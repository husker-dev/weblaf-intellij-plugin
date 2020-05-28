package com.husker.weblafplugin.skin.core.parameters;

import com.husker.weblafplugin.skin.core.IncludeElement;
import com.husker.weblafplugin.skin.core.IncludeSorting;
import com.husker.weblafplugin.skin.core.SkinEditor;
import com.husker.weblafplugin.skin.core.components.list.IncludeList;
import com.husker.weblafplugin.skin.core.components.parameter.Parameter;
import com.husker.weblafplugin.skin.core.dialogs.IncludeElementEditorDialog;
import com.husker.weblafplugin.skin.core.variables.ValueChangedListener;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.ToolbarDecorator;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.ArrayList;

public class IncludeListParameter extends Parameter {

    protected IncludeList list;
    private ArrayList<ValueChangedListener> listeners = new ArrayList<>();

    public IncludeListParameter(String name) {
        super(name);
    }

    public void onInit(){
        SkinEditor editor = getSkinEditor();
        Project project = editor.getProject();

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(list = new IncludeList(getSkinEditor()));
        decorator.setPreferredSize(new Dimension(440, 200));
        decorator.setAddAction(e -> {
            IncludeElement newElement = new IncludeElementEditorDialog(project, editor.getClassPath()){{
                setBlackList(getExistingElements());
            }}.getIncludeElement();

            if(newElement != null) {
                ArrayList<IncludeElement> elements = new ArrayList<>(list.getModel().getItems());
                elements.add(newElement);
                list.setContent(elements.toArray(new IncludeElement[0]));

                list.grabFocus();
                list.setSelectedValue(newElement, true);
            }
        });
        decorator.addExtraAction(new AnActionButton("Sort", AllIcons.ObjectBrowser.SortByType) {
            public void actionPerformed(AnActionEvent e) {
                ArrayList<IncludeElement> elements = new ArrayList<>(list.getModel().getItems());
                elements = new ArrayList<>(new IncludeSorting().sort(elements));
                list.setContent(elements.toArray(new IncludeElement[0]));
                for(ValueChangedListener listener : listeners)
                    listener.changed(list.getModel().getItems().toArray(new IncludeElement[0]));
            }
        });

        list.getModel().addListDataListener(new ListDataListener() {
            public void intervalAdded(ListDataEvent listDataEvent) {
                for(ValueChangedListener listener : listeners)
                    listener.changed(list.getModel().getItems().toArray(new IncludeElement[0]));
            }
            public void intervalRemoved(ListDataEvent listDataEvent) {
                for(ValueChangedListener listener : listeners)
                    listener.changed(list.getModel().getItems().toArray(new IncludeElement[0]));
            }
            public void contentsChanged(ListDataEvent listDataEvent) {
                for(ValueChangedListener listener : listeners)
                    listener.changed(list.getModel().getItems().toArray(new IncludeElement[0]));
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
        list.setContent((IncludeElement[]) value);
    }

    public void addValueChangedListener(ValueChangedListener listener) {
        listeners.add(listener);
    }

    public boolean haveErrors() {
        return list.testForError();
    }


    private String[] getExistingElements(){
        IncludeElement[] elements = list.getModel().getItems().toArray(new IncludeElement[0]);

        String[] path = new String[elements.length];

        for(int i = 0; i < elements.length; i++)
            path[i] = elements[i].getFullPath();
        return path;
    }



}
