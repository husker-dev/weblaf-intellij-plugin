package com.husker.weblafplugin.core.components.list.classes;

import com.husker.weblafplugin.core.components.AutoSizedLabel;
import com.husker.weblafplugin.core.components.list.List;
import com.husker.weblafplugin.core.components.list.files.AbstractFileListElement;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;

public class ClassListElement extends AbstractFileListElement<String> {

    protected Project project;
    protected AutoSizedLabel class_path_label;
    protected AutoSizedLabel class_name;
    protected JLabel icon;

    public ClassListElement(Project project, String class_path) {
        super(class_path);
        this.project = project;
        setIcon(JavaClassFileType.INSTANCE.getIcon());
    }

    public void onNameLabelsInit() {
        String name, path;

        if(getContent().contains(".")) {
            name = getContent().split("\\.")[getContent().split("\\.").length - 1];
            path = " (" + getContent().replace("." + name, "") + ")";
        }else {
            name = getContent();
            path = "";
        }

        addToLeft(class_name = new AutoSizedLabel(name){{
            setVerticalTextPosition(CENTER);
            setForeground(UIUtil.getInactiveTextColor());
        }});
        addToLeft(class_path_label = new AutoSizedLabel(path){{
            setVerticalTextPosition(CENTER);
            setForeground(UIUtil.getInactiveTextColor());
        }});
    }

    public void updateColors(){
        class_path_label.setForeground(getAlternativeTextColor());
        class_name.setForeground(getTextColor());
        super.updateColors();
    }

    public Color getBackgroundColor() {
        if(getState() == List.ElementState.UNSELECTED && !hasError) {
            try {
                PsiClass clazz = Tools.getClassByPath(project, getContent());

                if (clazz.getContainingFile().getVirtualFile().getPath().contains(".jar!"))
                    return new JBColor(new Color(255, 255, 228), new Color(79, 75, 65));
            }catch (Exception ex){}
        }
        return super.getBackgroundColor();
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
