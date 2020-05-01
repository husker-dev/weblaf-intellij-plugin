package com.husker.weblafplugin.dialogs;

import com.husker.weblafplugin.tools.Tools;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SkinClassChooserDialog extends DialogWrapper {

    private final JPanel component;
    private final JBList fieldList;

    public SkinClassChooserDialog(Project project, Class clazz) {
        this(project, clazz.getName());
    }

    public SkinClassChooserDialog(Project project, String clazz) {
        super(project);
        setTitle("Select Skin Class");

        CollectionListModel<PsiClass> classes = new CollectionListModel<>(Tools.getExtendedClasses(project, clazz));
        fieldList = new JBList(classes);
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        new DoubleClickListener(){
            protected boolean onDoubleClick(MouseEvent event) {
                if (fieldList.getSelectedValuesList().size() > 0) {
                    doOKAction();
                    return true;
                }
                return false;
            }
        }.installOn(fieldList);

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        decorator.disableAddAction();
        decorator.disableRemoveAction();
        decorator.disableUpDownActions();

        component = decorator.createPanel();
        component.setPreferredSize(new Dimension(300, 300));

        init();
    }

    protected JComponent createCenterPanel() {
        return component;
    }

    public PsiClass getPsiClass(){
        show();
        if(isOK() && fieldList.getSelectedValuesList().size() == 1)
            return ((PsiClass)fieldList.getSelectedValue());
        return null;
    }

}
