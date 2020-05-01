package com.husker.weblafplugin.parameters;

import com.husker.weblafplugin.components.parameter.Parameter;
import com.husker.weblafplugin.components.ResourcesList;
import com.husker.weblafplugin.variables.ValueChangedListener;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;

public class ResourceListParameter extends Parameter {

    protected ResourcesList list;

    protected String bound_read_head = null;

    public ResourceListParameter(String name, FileType fileType) {
        super(name);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        panel.add(new JBScrollPane(list = new ResourcesList(fileType)){{
            setPreferredSize(new Dimension(470, 400));
        }});
        panel.setBorder(BorderFactory.createEmptyBorder(5, 3, 0, 0));
        add(panel);
    }

    public void addValueChangedListener(ValueChangedListener listener) {
        // TODO
    }

    public void onValueApplying(Object value) {
        list.apply(getSkinEditor().getResourcePath(), (String[]) value);
    }

    public boolean haveErrors() {
        list.setResourcePath(getSkinEditor().getResourcePath());
        list.update();
        return false;
    }

    public void bindReadHead(String title){
        bound_read_head = title;
    }

}
