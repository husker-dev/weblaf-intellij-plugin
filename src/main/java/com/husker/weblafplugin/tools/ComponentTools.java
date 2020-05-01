package com.husker.weblafplugin.tools;

import java.awt.*;

public class ComponentTools {

    public static void setWidth(Component component, int width){
        component.setMaximumSize(new Dimension(width, component.getMaximumSize().height));
        component.setPreferredSize(new Dimension(width, component.getPreferredSize().height));
    }

    public static void setHeight(Component component, int height){
        component.setMaximumSize(new Dimension(component.getMaximumSize().width, height));
        component.setPreferredSize(new Dimension(component.getPreferredSize().width, height));
    }

    public static void setSize(Component component, int width, int height){
        setWidth(component, width);
        setHeight(component, height);
    }
}
