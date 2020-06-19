package com.husker.weblafplugin.core.components.textfield.magic;

import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;

public abstract class MagicContent extends JPanel {

    private MagicTextField textField;

    public abstract void update(String text);

    public Color getBackgroundColor(){
        if(!getMagicTextField().isEnabled())
            return UIUtil.getInactiveTextFieldBackgroundColor();
        return UIUtil.getTextFieldBackground();
    }

    public void setMagicTextField(MagicTextField textField){
        this.textField = textField;
    }

    public MagicTextField getMagicTextField(){
        return textField;
    }
}
