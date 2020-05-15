package com.husker.weblafplugin.core.components.textfield.magic;

import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;

public abstract class MagicContent extends JPanel {

    public abstract void update(String text);

    public Color getBackgroundColor(){
        return UIUtil.getTextFieldBackground();
    }
}
