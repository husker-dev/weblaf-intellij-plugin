package com.husker.weblafplugin.core.components.list;



import com.husker.weblafplugin.core.components.scroll.ScrollFitApplier;
import com.husker.weblafplugin.core.tools.DragSupport;
import com.husker.weblafplugin.core.tools.DropSupport;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.husker.weblafplugin.core.components.list.List.ElementState.*;
import static java.awt.FlowLayout.*;

public abstract class ListElement<T> extends JPanel implements ScrollFitApplier {

    public enum DropSide{
        TOP,
        BOTTOM,
        NOTHING
    }

    private List.ElementState state = List.ElementState.UNSELECTED;
    private JPanel left, right;
    private boolean dropHovered = false;

    private DropSide dropSide = DropSide.NOTHING;

    private T content;

    public ListElement(T content){
        this.content = content;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 20));

        add(left = new JPanel(){{
            setLayout(new FlowLayout(LEFT, 0, 0));
        }}, BorderLayout.WEST);
        add(right = new JPanel(){{
            setLayout(new FlowLayout(RIGHT, 0, 0));
        }}, BorderLayout.EAST);

        new DragSupport(this, content){{
            addDragSupportListener(() -> getList().setDraggingElement(ListElement.this));
        }};
        new DropSupport(this, content.getClass()){{
            addDropListener(transferred -> {
                if(transferred == this)
                    return;

                int index = -1;
                if(getDropSide() == ListElement.DropSide.TOP)
                    index = getList().getElementIndex(ListElement.this);
                if(getDropSide() == ListElement.DropSide.BOTTOM)
                    index = getList().getElementIndex(ListElement.this) + 1;

                getList().setDraggingElement(null);
                setDropHovered(false);
                onDropEvent();
                getList().repaint();

                getList().dragEvent();
                getList().reorderEvent(getList().getContentIndex((T)transferred), index);
            });
            addMoveListener(() -> {
                onDropEvent();
                getList().repaint();
                getList().dragEvent();
            });
            addHoverListener(hovered -> {
                setDropHovered(hovered);
                onDropEvent();
                getList().repaint();
                getList().dragEvent();
            });
        }};

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                getList().setSelectedElement(ListElement.this);
                getList().grabFocus();
            }
        });
    }

    public void addToLeft(Component component){
        left.add(component);
    }

    public void addToRight(Component component){
        right.add(component);
    }

    public void setState(List.ElementState state){
        if(this.state == state)
            return;

        this.state = state;
        ApplicationManager.getApplication().invokeLater(this::updateColors);
    }

    public Color getTextColor(){
        if(getState() == SELECTED_FOCUSED)
            return UIUtil.getTreeForeground(true, true);
        if(getState() == SELECTED_UNFOCUSED)
            return UIUtil.getTreeForeground(true, false);
        if(getState() == UNSELECTED)
            return UIUtil.getTreeForeground(false, false);
        return UIUtil.getActiveTextColor();
    }

    public Color getAlternativeTextColor(){
        if(getState() == SELECTED_FOCUSED)
            return getTextColor();
        else
            return UIUtil.getInactiveTextColor();
    }

    public Color getErrorColor(){
        return new JBColor(new Color(75, 45, 45), new Color(75, 45, 45));
    }

    public Color getBackgroundColor(){
        switch (state){
            case SELECTED_UNFOCUSED:
                return UIUtil.getListSelectionBackground(false);
            case UNSELECTED:
                return UIUtil.getListBackground();
            case SELECTED_FOCUSED:
                return UIUtil.getListSelectionBackground(true);
        }
        return UIUtil.getListBackground();
    }

    public void updateColors(){
        setBackground(getBackgroundColor());
        left.setBackground(getBackgroundColor());
        right.setBackground(getBackgroundColor());
    }

    public List.ElementState getState(){
        return state;
    }

    public void paint(Graphics gr){
        super.paint(gr);
        drawDND(gr);
    }

    // DND = Drag And Drop
    public void drawDND(Graphics gr){
        if(getList().getDraggingElement() == this)
            return;

        ListElement<T> next_element = getNextElement();
        ListElement<T> prev_element = getPrevElement();
        boolean isNextDragging = next_element == getList().getDraggingElement();
        boolean isPrevDragging = prev_element == getList().getDraggingElement();

        int size = 1;

        gr.setColor(JBColor.red);
        if(dropSide == DropSide.TOP && !isPrevDragging)
            gr.fillRect(0, 0, getWidth(), size);
        if(dropSide == DropSide.BOTTOM && !isNextDragging)
            gr.fillRect(0, getHeight() - 1, getWidth(), getHeight() - 1 - size);

        if(next_element != null && !isNextDragging && next_element.getDropSide() == DropSide.TOP)
            gr.fillRect(0, getHeight() - 1, getWidth(), getHeight() - 1 - size);

        if(prev_element != null && !isPrevDragging && prev_element.getDropSide() == DropSide.BOTTOM)
            gr.fillRect(0, 0, getWidth(), size);
    }

    public DropSide getDropSide(){
        return dropSide;
    }

    public void onDropEvent(){
        if(!isDropHovered()){
            dropSide = DropSide.NOTHING;
            return;
        }

        Point position = getMousePosition(true);
        if(position != null) {
            if (position.y < getHeight() / 2)
                dropSide = DropSide.TOP;
            else
                dropSide = DropSide.BOTTOM;
        }else
            dropSide = DropSide.NOTHING;
    }

    public ListElement<T> getPrevElement(){
        List<T> list = getList();
        if (list != null) {
            int index = list.getElementIndex(this);
            if (index > 0)
                return list.getListElementAt(index - 1);
        }
        return null;
    }

    public ListElement<T> getNextElement(){
        List<T> list = getList();
        if (list != null) {
            int index = list.getElementIndex(this);
            if (index != -1 && index < list.getElementCount() - 1)
                return list.getListElementAt(index + 1);
        }
        return null;
    }

    public List<T> getList(){
        try {
            return (List<T>) getParent();
        }catch (Exception ex){
            return null;
        }
    }

    public T getContent(){
        return content;
    }

    public boolean isDropHovered() {
        return dropHovered;
    }
    public void setDropHovered(boolean dropHovered) {
        this.dropHovered = dropHovered;
    }

    public void fitContent(boolean barVisible) {
        right.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, barVisible ? 10 : 0));
    }

    public boolean equals(Object obj) {
        if(obj instanceof ListElement)
            return ((ListElement)obj).getContent().equals(getContent());
        else
            return false;
    }

    public String toString() {
        return "ListElement{" +
                "state=" + state +
                ", dropHovered=" + dropHovered +
                ", dropSide=" + dropSide +
                '}';
    }
}
