package com.husker.weblafplugin.core.components.textfield.magic.impl;

import com.husker.weblafplugin.core.components.textfield.magic.MagicContent;
import com.husker.weblafplugin.core.tools.Tools;

import javax.swing.*;
import java.awt.*;

public abstract class MagicIconContent extends MagicContent {

    private JLabel label_icon;
    private Icon icon;

    {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        add(label_icon = new JLabel(){{
            setVerticalAlignment(CENTER);
            setHorizontalAlignment(CENTER);
            setPreferredSize(new Dimension(24, 20));
        }});
    }

    public void update(String text){
        updateIcon();
    }

    public void setIcon(Icon icon){
        this.icon = icon;
        updateIcon();
    }

    private void updateIcon(){
        if(getMagicTextField().isEnabled())
            label_icon.setIcon(icon);
        else
            label_icon.setIcon(Tools.getDarkerIcon(icon));
    }
}
