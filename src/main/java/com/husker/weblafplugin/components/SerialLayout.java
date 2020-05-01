package com.husker.weblafplugin.components;

import java.awt.*;

public class SerialLayout implements LayoutManager {

    protected int gap;

    public SerialLayout(int gap){
        this.gap = gap;
    }

    public SerialLayout(){
        this(0);
    }

    public void addLayoutComponent(String s, Component component) {
        update(component.getParent());
    }

    public void removeLayoutComponent(Component component) {
        update(component.getParent());
    }

    public Dimension preferredLayoutSize(Container container) {
        int max_height = 0;
        int width = 0;
        for(Component component : container.getComponents()) {
            if(!component.isVisible())
                continue;

            max_height = (int) Math.max(max_height, component.getPreferredSize().getHeight());
            width += component.getPreferredSize().getWidth() + gap;
        }

        return new Dimension(width, max_height);
    }

    public Dimension minimumLayoutSize(Container container) {
        int max_height = 0;
        int width = 0;
        for(Component component : container.getComponents()) {
            if(!component.isVisible())
                continue;

            max_height = (int) Math.max(max_height, component.getMinimumSize().getHeight());
            width += component.getMinimumSize().getWidth() + gap;
        }

        return new Dimension(width, max_height);
    }

    public void layoutContainer(Container container) {
        update(container);
    }

    public void update(Container container){
        synchronized (container.getTreeLock()){
            int max_height = 0;
            for(Component component : container.getComponents()) {
                if(!component.isVisible())
                    continue;

                max_height = (int) Math.max(max_height, component.getPreferredSize().getHeight());
            }

            int cur_x = 0;
            for(Component component : container.getComponents()) {
                if(!component.isVisible())
                    continue;

                component.setBounds(cur_x, 0, (int) component.getPreferredSize().getWidth(), max_height);
                cur_x += component.getPreferredSize().getWidth() + gap;
            }
        }

    }
}
