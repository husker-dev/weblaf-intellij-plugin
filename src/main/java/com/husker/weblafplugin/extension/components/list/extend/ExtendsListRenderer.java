package com.husker.weblafplugin.extension.components.list.extend;

import com.husker.weblafplugin.core.components.list.FileCellRenderer;
import com.husker.weblafplugin.core.components.list.FileList;
import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.*;

public class ExtendsListRenderer extends FileCellRenderer<String> {

    public void initComponents() {
        setPreferredSize(new Dimension(0, 25));

        addIcon("icon");
        addLabel("title");
        addLabel("id", false);

        addLabelToRight("author", false);
    }

    public String getFilePath() {
        if(getCached("exist") != null && getCached("exist", Boolean.class)){
            return getCached("path", String.class);
        }else
            return super.getFilePath();
    }

    public void updateContent() {
        if(getCached("exist") != null && getCached("exist", Boolean.class)){
            Icon icon = getCached("icon", Icon.class);
            String title = getCached("title", String.class);
            String id = getElement();
            String author = getCached("author", String.class);

            setIcon("icon", (icon != null) ? icon : AllIcons.Nodes.Unknown);
            setLabelText("author", (author != null) ? ("By " + author) : (""));

            if(title != null) {
                setLabelText("title", title);
                setLabelText("id", "(" + id + ")");
            }else{
                setLabelText("title", id);
                setLabelText("id", "");
            }
        }else{
            setIcon("icon", AllIcons.Nodes.Unknown);
            setLabelText("title", getElement());

            setLabelText("id", "");
            setLabelText("author", "");
        }
    }

    public <T> T getCached(String tag,  Class<? extends T> type){
        Object o = getCached(tag);
        if(o == null)
            return null;
        return (T) o;
    }

    public Object getCached(String tag){
        return getList().getCached(tag, getElement());
    }

    public FileList<String> getList(){
        if(super.getList() instanceof FileList)
            return (FileList<String>) super.getList();
        else
            return null;
    }

    public boolean haveError() {
        return false;
    }
}
