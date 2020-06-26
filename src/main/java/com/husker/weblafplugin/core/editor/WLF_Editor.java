package com.husker.weblafplugin.core.editor;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;

public abstract class WLF_Editor extends JPanel {

    private final Project project;
    private final VirtualFile file;

    private Icon icon;
    private int preferredIndex = -1;
    private String tip = null;
    private String title = null;

    public WLF_Editor(Project project, VirtualFile file){
        this.project = project;
        this.file = file;
    }

    public void setPreferredTabIndex(int index){
        this.preferredIndex = index;
    }

    public int getPreferredTabIndex(WLF_Editor[] list){
        if(preferredIndex == -1)
            return list.length;
        else
            return preferredIndex;
    }

    public void setIcon(Icon icon){
        this.icon = icon;
    }

    public Icon getIcon(){
        return icon;
    }

    public void setTip(String tip){
        this.tip = tip;
    }

    public String getTip(){
        if(tip == null)
            return getTitle();
        else
            return tip;
    }

    public Project getProject(){
        return project;
    }

    public VirtualFile getVirtualFile(){
        return file;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        if(title == null)
            return toString();
        else
            return title;
    }

    public abstract void onUpdate();
    public abstract void dispose();
}
