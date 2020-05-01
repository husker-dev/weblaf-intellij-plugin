package com.husker.weblafplugin.components;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.ui.VerticalFlowLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ResourcesList extends JPanel {

    public enum ElementState{
        SELECTED_UNFOCUSED,
        UNSELECTED,
        SELECTED_FOCUSED
    }

    protected FileType fileType;
    protected ArrayList<ResourceElement> elements = new ArrayList<>();
    protected ArrayList<ElementSelectedListener> selected_listeners = new ArrayList<>();
    protected ResourceElement selected;

    public ResourcesList(FileType fileType){
        this.fileType = fileType;
        setLayout(new VerticalFlowLayout(0, 0));

        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if(selected != null)
                    selected.setState(ElementState.SELECTED_FOCUSED);
            }
            public void focusLost(FocusEvent e) {
                if(selected != null)
                    selected.setState(ElementState.SELECTED_UNFOCUSED);
            }
        });
    }

    public void apply(String resource_path, String[] paths){
        removeAll();
        elements.clear();

        for(String path : paths) {
            ResourceElement element = new ResourceElement(fileType, resource_path, path);
            configureElement(element);
        }
        update();
    }

    public void setResourcePath(String resource_path){
        for(Component component : getComponents()) {
            if (component instanceof ResourceElement) {
                ResourceElement element = (ResourceElement) component;
                element.setResourcePath(resource_path);
            }
        }
    }

    public void update(){
        new Thread(() -> {
            for(Component component : getComponents()) {
                if (component instanceof ResourceElement) {
                    ResourceElement element = (ResourceElement) component;
                    if(element.hasErrors())
                        element.onError();
                    else
                        element.onErrorReset();
                }
            }
        }).start();
    }

    public ResourceElement[] getElements(){
        return elements.toArray(new ResourceElement[0]);
    }

    public ResourceElement getElement(int index){
        return elements.get(index);
    }

    void configureElement(ResourceElement element){
        element.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent mouseEvent) {

            }
            public void mousePressed(MouseEvent mouseEvent) {
                if(selected != null)
                    selected.setState(ElementState.UNSELECTED);
                selected = element;
                selected.setState(ElementState.SELECTED_FOCUSED);
                grabFocus();
            }
            public void mouseReleased(MouseEvent mouseEvent) {

            }
            public void mouseEntered(MouseEvent mouseEvent) {

            }
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        elements.add(element);
        add(element);
    }

    public void addElementSelectedListener(ElementSelectedListener listener){
        selected_listeners.add(listener);
    }


    public interface ElementSelectedListener {
        void selected(int index);
    }

}
