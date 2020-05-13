package com.husker.weblafplugin.core.skin.components.parameter;

import com.husker.weblafplugin.core.components.SerialLayout;
import com.husker.weblafplugin.core.skin.components.VariableApplier;

import com.husker.weblafplugin.core.skin.AbstractSkinEditor;
import com.husker.weblafplugin.core.skin.managers.SkinEditorManager;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;

public abstract class Parameter extends JPanel implements VariableApplier {

    public static final int DEFAULT_WIDTH = 470;
    public static final int DEFAULT_HEIGHT = 25;
    public static final int DEFAULT_INDENT = 80;

    protected String name;
    protected JLabel error;

    public Parameter(String name){
        this.name = name;

        setLayout(new SerialLayout(5));
        add(error = new JLabel(" "){{
            setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
            setPreferredSize(new Dimension(6, DEFAULT_HEIGHT));
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(TOP);
            setForeground(JBColor.red);
        }});
        add(new JLabel(name + ":"){{
            setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
            setVerticalAlignment(TOP);
            setPreferredSize(new Dimension(DEFAULT_INDENT, DEFAULT_HEIGHT));
        }});
    }

    public void setValue(Object value) {
        onValueApplying(value);
        onError(haveErrors());
    }

    public abstract void onValueApplying(Object value);

    public AbstractSkinEditor getSkinEditor(){
        return SkinEditorManager.get(this);
    }

    public abstract boolean haveErrors();

    public void onError(boolean haveError) {
        if(haveError)
            error.setText("*");
        else
            error.setText("");
    }
}
