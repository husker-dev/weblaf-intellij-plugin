package com.husker.weblafplugin.core.components.textfield.magic.path;

import com.husker.weblafplugin.core.components.textfield.magic.MagicContent;
import com.husker.weblafplugin.core.components.textfield.magic.icon.IconMagicContent;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.ui.IconManager;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;

public class PathMagicContent extends IconMagicContent {

    private JLabel name, path;

    {
        add(name = new JLabel(){{
            setForeground(UIUtil.getTextAreaForeground());
        }});
        add(path = new JLabel(){{
            setForeground(UIUtil.getInactiveTextColor());
        }});
    }

    public void update(String text) {
        try{
            text = text.replace("\\", "/");

            setIcon(FileTypeManager.getInstance().getFileTypeByFileName(text).getIcon());

            String name_text;
            String path_text;
            if(text.contains("/")){
                path_text = " (" + text.substring(0, text.lastIndexOf("/")) + ")";
                name_text = text.substring(text.lastIndexOf("/") + 1);
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
