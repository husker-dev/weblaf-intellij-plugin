package com.husker.weblafplugin.core.components.textfield.magic.classes;

import com.husker.weblafplugin.core.components.textfield.magic.icon.IconMagicContent;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;

public class ClassMagicContent extends IconMagicContent {

    private Project project;
    private JLabel name, path;

    public ClassMagicContent(Project project){
        this.project = project;
        add(name = new JLabel(){{
            setForeground(UIUtil.getTextAreaForeground());
        }});
        add(path = new JLabel(){{
            setForeground(UIUtil.getInactiveTextColor());
        }});
    }

    public void update(String text) {
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
            path.setText(path_text);
        }catch (Exception ex){
            ex.printStackTrace();
            setIcon(JavaClassFileType.INSTANCE.getIcon());
        }
    }
}
