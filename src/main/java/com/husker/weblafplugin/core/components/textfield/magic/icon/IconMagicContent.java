package com.husker.weblafplugin.core.components.textfield.magic.icon;

import com.husker.weblafplugin.core.components.textfield.magic.MagicContent;

import javax.swing.*;
import java.awt.*;

public abstract class IconMagicContent extends MagicContent {

    private JLabel icon;

    {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        add(icon = new JLabel(){{
            setVerticalAlignment(CENTER);
            setHorizontalAlignment(CENTER);
            setPreferredSize(new Dimension(24, 20));
        }});
    }

    public void setIcon(Icon icon){
        this.icon.setIcon(icon);
    }
}
