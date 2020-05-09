package com.husker.weblafplugin.components.list;


import com.husker.weblafplugin.components.list.text.TextListElement;
import com.husker.weblafplugin.tools.DragSupport;
import com.husker.weblafplugin.tools.DropSupport;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.AnActionButton;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class List<T> extends JComponent {

    public enum ElementState{
        SELECTED_UNFOCUSED,
        UNSELECTED,
        SELECTED_FOCUSED
    }

    protected ArrayList<ElementSelectedListener> listeners_selected = new ArrayList<>();
    protected ArrayList<ElementReorderedListener> listeners_reordered = new ArrayList<>();
    protected ArrayList<ListDragListener> listeners_dragging = new ArrayList<>();

    protected ArrayList<ListElement<T>> elements = new ArrayList<>();
    protected ListElement<T> selectedElement;
    protected ListElementGenerator<T> generator = object -> new TextListElement(object.toString());

    protected T[] content;

    protected ListElement<T> dragging_element;

    public List(){
        setLayout(new VerticalFlowLayout(0, 0));

        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if(selectedElement != null)
                    selectedElement.setState(ElementState.SELECTED_FOCUSED);
            }
            public void focusLost(FocusEvent e) {
                if(selectedElement != null)
                    selectedElement.setState(ElementState.SELECTED_UNFOCUSED);
            }
        });
    }

    public void setListElementGenerator(ListElementGenerator<T> generator){
        this.generator = generator;
    }

    public void setContent(T[] content){
        if(Arrays.equals(this.content, content))
            return;
        this.content = content;

        removeAll();
        elements.clear();

        for(T object : content) {
            ListElement<T> element = generator.generateListElement(object);

            new DragSupport(element, object){{
                addDragSupportListener(() -> {
                    dragging_element = element;
                });
            }};
            new DropSupport(element, object.getClass()){{
                addDropListener(transferred -> {
                    if(transferred == element)
                        return;

                    int index = -1;
                    if(element.getDropSide() == ListElement.DropSide.TOP)
                        index = getElementIndex(element);
                    if(element.getDropSide() == ListElement.DropSide.BOTTOM)
                        index = getElementIndex(element) + 1;

                    dragging_element = null;
                    element.setDropHovered(false);
                    element.onDropMoving();
                    repaint();

                    for(ElementReorderedListener listener : listeners_reordered)
                        listener.reordered(getContentIndex((T)transferred), index);
                    dragEvent();
                });
                addMoveListener(() -> {
                    element.onDropMoving();
                    repaint();
                    dragEvent();
                });
                addHoverListener(hovered -> {
                    element.setDropHovered(hovered);
                    element.onDropMoving();
                    repaint();
                    dragEvent();
                });
            }};

            element.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent mouseEvent) {
                    setSelectedElement(element);
                    grabFocus();
                }
            });
            elements.add(element);
            add(element);
        }

        doLayout();

        if(selectedElement != null){
            boolean selected = false;
            for(ListElement<T> element : elements) {
                if (element.equals(selectedElement)) {
                    setSelectedElement(element);
                    selected = true;
                    break;
                }
            }
            if(!selected)
                setSelectedElement(null);
        }
    }

    private void dragEvent(){
        for(ListDragListener listener : listeners_dragging)
            listener.event();
    }

    public T[] getContent(){
        return content;
    }

    public void addElementSelectedListener(ElementSelectedListener listener){
        listeners_selected.add(listener);
    }
    public void addElementReorderedListener(ElementReorderedListener listener){
        listeners_reordered.add(listener);
    }
    public void addDragListener(ListDragListener listener){
        listeners_dragging.add(listener);
    }

    public void setSelectedElement(ListElement<T> element){
        if(selectedElement != null && selectedElement == element)
            return;

        if(selectedElement != null)
            selectedElement.setState(ElementState.UNSELECTED);
        selectedElement = element;
        if(selectedElement != null) {
            selectedElement.setState(ElementState.SELECTED_FOCUSED);
            scrollRectToVisible(selectedElement.getBounds());
        }
        for(ElementSelectedListener listener : listeners_selected)
            listener.selected(element);

    }

    public ListElement<T> getSelectedElement(){
        return selectedElement;
    }

    public int getSelectedIndex(){
        return getElementIndex(getSelectedElement());
    }

    public int getContentIndex(T object){
        return Arrays.asList(content).indexOf(object);
    }

    public int getElementIndex(ListElement<T> object){
        for(int i = 0; i < getComponentCount(); i++)
            if(getComponent(i) == object)
                return i;
        return -1;
    }

    public int getElementCount(){
        if(content == null)
            return 0;
        return content.length;
    }

    public ListElement<T>[] getElements(){
        return (ListElement<T>[]) getComponents();
    }

    public T getContentAt(int index){
        return content[index];
    }

    public ListElement<T> getDraggingElement(){
        return dragging_element;
    }

    public ListElement<T> getListElementAt(int index){
        return (ListElement<T>)getComponent(index);
    }
}
