package com.husker.weblafplugin.core.components.textfield.magic.impl;

import com.husker.weblafplugin.core.components.textfield.magic.impl.MagicIconContent;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.UnknownFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;

public class MagicPathContent extends MagicIconContent {

    private JLabel name, path;
    private Project project;

    public MagicPathContent(){
        this(null);
    }
    public MagicPathContent(Project project){
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
            text = text.replace("\\", "/");

            VirtualFile file = Tools.getVirtualFile(text);
            if(file != null && project != null) {
                PsiFile psiFile = Tools.getPsi(project, file);
                if(psiFile != null)
                    setIcon(psiFile.getIcon(0));
                else
                    setDefaultIcon(text);
            }else
                setDefaultIcon(text);

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
            setIcon(UnknownFileType.INSTANCE.getIcon());
        }
    }

    private void setDefaultIcon(String path){
        setIcon(FileTypeManager.getInstance().getFileTypeByFileName(path).getIcon());
    }
}
