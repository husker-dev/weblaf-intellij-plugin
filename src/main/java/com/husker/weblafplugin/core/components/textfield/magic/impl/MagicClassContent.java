package com.husker.weblafplugin.core.components.textfield.magic.impl;

import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;

public class MagicClassContent extends MagicIconContent {

    private Project project;
    private JLabel name, path;

    private Icon defaultIcon;

    public MagicClassContent(Project project){
        this.project = project;
        add(name = new JLabel(){{
            setForeground(UIUtil.getTextAreaForeground());
        }});
        add(path = new JLabel(){{
            setForeground(UIUtil.getInactiveTextColor());
        }});
    }

    public void update(String text) {
        super.update(text);
        try{
            PsiClass clazz = Tools.getClassByPath(project, text);
            if(clazz != null)
                setIcon(clazz.getIcon(0));
            else
                setIcon(JavaClassFileType.INSTANCE.getIcon());

            String name_text;
            String path_text;
            if(text.contains(".")){
                path_text = " (" + text.substring(0, text.lastIndexOf(".")) + ")";
                name_text = text.substring(text.lastIndexOf(".") + 1);
            }else {
                name_text = text;
                path_text = "";
            }

            name.setText(name_text);
            name.setForeground(getMagicTextField().isEnabled() ? UIUtil.getTextAreaForeground() : UIUtil.getInactiveTextColor());
            path.setText(path_text);
        }catch (Exception ex){
            ex.printStackTrace();
            setIcon(JavaClassFileType.INSTANCE.getIcon());
        }
    }

    public void setDefaultIcon(Icon icon){
        defaultIcon = icon;
        update(getMagicTextField().getText());
    }

    public void setIcon(Icon icon){
        if(defaultIcon == null)
            super.setIcon(icon);
        else
            super.setIcon(defaultIcon);
    }
}
