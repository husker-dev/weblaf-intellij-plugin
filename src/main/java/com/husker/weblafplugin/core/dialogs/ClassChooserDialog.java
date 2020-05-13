package com.husker.weblafplugin.core.dialogs;

import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.lang.jvm.JvmClassKind;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.util.PsiUtil;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ClassChooserDialog extends DialogWrapper {

    private final JPanel component;
    private final JBList<PsiClass> fieldList;

    public ClassChooserDialog(Project project, String title, Class<?> clazz) {
        this(project, title, clazz.getName());
    }

    public ClassChooserDialog(Project project, String title, String clazz) {
        super(project);
        setTitle(title);

        CollectionListModel<PsiClass> classes = new CollectionListModel<>();
        fieldList = new JBList<>(classes);
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
        component.setPreferredSize(new Dimension(450, 250));

        init();

        final ArrayList<PsiClass> found = new ArrayList<>();
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            ApplicationManager.getApplication().runReadAction(() -> {

                Tools.getExtendedClasses(project, clazz, psiClass -> {

                    if(psiClass.getClassKind().equals(JvmClassKind.CLASS) && !PsiUtil.isAbstractClass(psiClass)){
                        found.add(psiClass);
                        fieldList.setListData(found.toArray(new PsiClass[0]));
                    }
                });
            });
        });


    }

    protected JComponent createCenterPanel() {
        return component;
    }

    public PsiClass getPsiClass(){
        show();
        if(isOK() && fieldList.getSelectedValuesList().size() == 1)
            return fieldList.getSelectedValue();
        return null;
    }

}
