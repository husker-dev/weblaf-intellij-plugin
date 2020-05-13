package com.husker.weblafplugin.core.skin.parameters;

import com.husker.weblafplugin.core.components.control.DefaultListControl;
import com.husker.weblafplugin.core.components.list.files.AbstractFileList;
import com.husker.weblafplugin.core.skin.components.parameter.Parameter;
import com.husker.weblafplugin.core.skin.variables.ValueChangedListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class FileListParameter<T> extends Parameter {

    private final ArrayList<ValueChangedListener> listeners = new ArrayList<>();
    protected AbstractFileList<T> list;

    public FileListParameter(String name) {
        super(name);
    }

    public void setListControl(DefaultListControl<T> control){
        if(!(control.getList() instanceof AbstractFileList))
            throw new UnsupportedOperationException("List type must be AbstractFileList");

        this.list = (AbstractFileList<T>) control.getList();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));

        panel.add(control);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 3, 0, 0));
        add(panel);

        control.addContentChangedListener(elements -> {
            for(ValueChangedListener listener : listeners)
                listener.changed(elements);
        });
    }

    public void onValueApplying(Object value) {
        list.setContent((T[]) value);
    }

    public boolean haveErrors() {
        list.testForExistence();
        return false;
    }

    public void addValueChangedListener(ValueChangedListener listener) {
        listeners.add(listener);
    }
}
