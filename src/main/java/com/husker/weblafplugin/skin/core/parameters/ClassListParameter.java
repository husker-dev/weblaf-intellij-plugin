package com.husker.weblafplugin.skin.core.parameters;

import com.husker.weblafplugin.core.components.list.classes.ClassList;
import com.husker.weblafplugin.core.dialogs.ClassChooserDialog;
import com.husker.weblafplugin.skin.core.SkinEditor;
import com.husker.weblafplugin.skin.core.components.parameter.Parameter;
import com.husker.weblafplugin.skin.core.variables.ValueChangedListener;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.ui.ToolbarDecorator;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.ArrayList;

public class ClassListParameter extends Parameter {

    protected ClassList list;
    private ArrayList<ValueChangedListener> listeners = new ArrayList<>();

    public ClassListParameter(String name){
        super(name);
    }

    public void onInit(){
        SkinEditor editor = getSkinEditor();
        Project project = editor.getProject();

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(list = new ClassList(project));
        decorator.setPreferredSize(new Dimension(440, 100));
        decorator.setAddAction(e -> {
            PsiClass newElement = new ClassChooserDialog(project, "Choose IconSet class", "com.alee.managers.icon.set.IconSet"){{
                addBlackListClass("com.alee.managers.icon.set.RuntimeIconSet");
                addBlackListClass("com.alee.managers.icon.set.XmlIconSet");

                for(String class_path : list.getContent())
                    addBlackListClass(class_path);

            }}.getPsiClass();

            if(newElement != null) {
                ArrayList<String> elements = new ArrayList<>(list.getModel().getItems());
                elements.add(newElement.getQualifiedName());
                list.setContent(elements.toArray(new String[0]));

                list.grabFocus();
                list.setSelectedValue(newElement, true);
            }
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
        list.setContent((String[]) value);
    }

    public boolean haveErrors() {
        return list.testForError();
    }

    public void addValueChangedListener(ValueChangedListener listener) {
        listeners.add(listener);
    }


}
