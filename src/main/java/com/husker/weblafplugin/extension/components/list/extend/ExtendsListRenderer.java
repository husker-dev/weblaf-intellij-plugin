package com.husker.weblafplugin.extension.components.list.extend;

import com.husker.weblafplugin.core.components.list.FileCellRenderer;

import javax.swing.*;
import java.awt.*;

public class ExtendsListRenderer extends FileCellRenderer<String> {

    public void initComponents() {
        setPreferredSize(new Dimension(0, 30));

        addIcon("icon");
        addLabel("id");

        JLabel iconLabel = getIcon("icon");
        JLabel idLabel = getLabel("id");

        iconLabel.setPreferredSize(new Dimension((int)getPreferredSize().getHeight(), (int)getPreferredSize().getHeight()));
        idLabel.setPreferredSize(new Dimension(0, (int)getPreferredSize().getHeight()));
    }


    public void updateContent() {
        setLabelText("id", getElement());
        setIcon("icon", (Icon)((ExtendsList)getList()).getCached("icon", getElement()));

        getLabel("id").setPreferredSize(new Dimension((int)getList().getSize().getWidth(), (int)getPreferredSize().getHeight()));
    }


    public boolean haveError() {
        return false;
    }
}
