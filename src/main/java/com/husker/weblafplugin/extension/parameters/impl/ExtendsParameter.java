package com.husker.weblafplugin.extension.parameters.impl;

import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.core.managers.SimpleXmlParameterEditorManager;
import com.husker.weblafplugin.extension.components.list.extend.ExtendsList;
import com.husker.weblafplugin.skin.components.parameter.Parameter;
import com.husker.weblafplugin.skin.variables.ValueChangedListener;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ToolbarDecorator;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.ArrayList;

public class ExtendsParameter extends Parameter {

    protected ExtendsList list;
    private ArrayList<ValueChangedListener> listeners = new ArrayList<>();

    public ExtendsParameter(String name) {
        super(name);
    }

    public void onInit(){
        SimpleXmlParameterEditor editor = SimpleXmlParameterEditorManager.getByParameterContext(this);
        Project project = editor.getProject();

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(list = new ExtendsList(editor));
        decorator.setPreferredSize(new Dimension(440, 70));
        decorator.setAddAction(e -> {
            /*
            IncludeElement newElement = new IncludeElementEditorDialog(project, SkinEditorManager.getClassPath(editor)){{
                setBlackList(getExistingElements());
            }}.getIncludeElement();

            if(newElement != null) {
                ArrayList<IncludeElement> elements = new ArrayList<>(list.getModel().getItems());
                elements.add(newElement);
                list.setContent(elements.toArray(new IncludeElement[0]));

                list.grabFocus();
                list.setSelectedValue(newElement, true);
            }

             */
        });

        list.getModel().addListDataListener(new ListDataListener() {
            public void intervalAdded(ListDataEvent listDataEvent) {
                for(ValueChangedListener listener : listeners)
                    listener.changed(list.getModel().getItems().toArray(new String[0]));
            }
            public void intervalRemoved(ListDataEvent listDataEvent) {
                for(ValueChangedListener listener : listeners)
                    listener.changed(list.getModel().getItems().toArray(new String[0]));
            }
            public void contentsChanged(ListDataEvent listDataEvent) {
                for(ValueChangedListener listener : listeners)
                    listener.changed(list.getModel().getItems().toArray(new String[0]));
            }
        });


        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));

        panel.add(decorator.createPanel());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 3, 0, 0));
        add(panel);
    }

    public void onValueApplying(Object value) {
        if(value == null)
            return;
        list.setContent((String[]) value);
    }

    public void addValueChangedListener(ValueChangedListener listener) {
        listeners.add(listener);
    }

    public boolean haveErrors() {
        return list.testForError();
    }


    private String[] getExistingElements(){
        return list.getModel().getItems().toArray(new String[0]);
    }
}
