package com.husker.weblafplugin.components;

import com.husker.weblafplugin.components.ResourcesList;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;

public class ResourceElement extends JPanel {

    protected JLabel error;
    protected String resource_path;
    protected String path;
    protected ResourcesList.ElementState state = ResourcesList.ElementState.UNSELECTED;
    protected boolean hasError = false;

    public ResourceElement(FileType fileType, String resource_path, String path){
        this.resource_path = resource_path;
        this.path = path;

        setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 0));

        setLayout(new BorderLayout(4, 0));
        add(new JLabel(fileType.getIcon()), BorderLayout.WEST);
        add(new JLabel(path));
    }

    public void setState(ResourcesList.ElementState state){
        this.state = state;
        updateBackground();
    }

    public boolean hasErrors() {
        return !new File(resource_path  + "/" + path).exists();
    }

    public void onError() {
        hasError = true;
        updateBackground();
    }

    public void onErrorReset() {
        hasError = false;
        updateBackground();
    }

    public void updateBackground(){
        ApplicationManager.getApplication().invokeLater(() -> {
            switch (state){
                case SELECTED_UNFOCUSED:
                    setBackground(UIUtil.getListSelectionBackground(false));
                    break;
                case UNSELECTED:
                    if(hasError) {
                        setBackground(new Color(75, 45, 45));
                    }else
                        setBackground(UIUtil.getListBackground());
                    break;
                case SELECTED_FOCUSED:
                    setBackground(UIUtil.getListSelectionBackground(true));
                    break;
            }
        });
    }

    public void setResourcePath(String resource_path){
        this.resource_path = resource_path;
    }
}
