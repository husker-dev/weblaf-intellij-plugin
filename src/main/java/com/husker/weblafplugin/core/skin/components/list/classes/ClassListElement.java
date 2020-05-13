package com.husker.weblafplugin.core.skin.components.list.classes;

import com.husker.weblafplugin.core.components.AutoSizedLabel;
import com.husker.weblafplugin.core.components.list.files.AbstractFileListElement;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;

public class ClassListElement extends AbstractFileListElement<String> {

    protected Project project;
    protected AutoSizedLabel class_path_label;
    protected JLabel icon;

    public ClassListElement(Project project, String class_path) {
        super(class_path);
        this.project = project;
        setIcon(JavaClassFileType.INSTANCE.getIcon());
    }

    public void onNameLabelsInit() {
        addToLeft(class_path_label = new AutoSizedLabel(getContent()){{
            setVerticalTextPosition(CENTER);
            setForeground(UIUtil.getInactiveTextColor());
        }});
    }

    public void updateColors(){
        class_path_label.setForeground(getTextColor());

        super.updateColors();
    }

    public boolean testForExistence() {
        PsiClass clazz = Tools.getClassByPath(project, getContent());
        if(clazz != null)
            setIcon(clazz.getIcon(0));
        else
            setIcon(JavaClassFileType.INSTANCE.getIcon());
        return clazz == null;
    }
}
