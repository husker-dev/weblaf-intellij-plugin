package com.husker.weblafplugin.skin.core.components.control;

import com.alee.managers.icon.set.IconSet;
import com.husker.weblafplugin.core.components.control.DefaultListControl;
import com.husker.weblafplugin.core.components.list.classes.ClassList;
import com.husker.weblafplugin.core.dialogs.ClassChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

import java.awt.*;

public class ClassListControl extends DefaultListControl<String> {

    public ClassListControl(Project project, ClassList list) {
        super(list, () -> {
            ClassChooserDialog dialog = new ClassChooserDialog(project, "Select IconSet class", IconSet.class);
            dialog.addBlackListClass("com.alee.managers.icon.set.RuntimeIconSet");

            PsiClass clazz = dialog.getPsiClass();

            if(clazz != null)
                return clazz.getQualifiedName();
            else
                return null;
        });

        setPreferredSize(new Dimension(470, 100));
    }
}
