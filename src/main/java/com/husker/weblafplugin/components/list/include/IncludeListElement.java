package com.husker.weblafplugin.components.list.include;

import com.husker.weblafplugin.components.AutoSizedLabel;
import com.husker.weblafplugin.components.list.ListElement;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static com.husker.weblafplugin.components.list.List.ElementState.*;

public class IncludeListElement extends ListElement<IncludeElement> {

    protected IncludeElement includeElement;
    protected boolean hasError = false;

    protected AutoSizedLabel passive_path, path, extension;
    protected JLabel icon, nearClass;

    public IncludeListElement(IncludeElement element){
        super(element);
        includeElement = element;

        // Icon
        addToLeft(icon = new JLabel(){{
            setVerticalAlignment(CENTER);
            setHorizontalAlignment(CENTER);
            setPreferredSize(new Dimension(24, 20));
        }});

        // Passive path
        addToLeft(passive_path = new AutoSizedLabel(includeElement.getPassivePath()){{
            setVerticalTextPosition(CENTER);
            setForeground(UIUtil.getInactiveTextColor());
        }});

        // Path
        String name = includeElement.getPath();
        name = name.replaceFirst(includeElement.getPassivePath(), "");
        name = name.substring(0, name.lastIndexOf("."));
        addToLeft(path = new AutoSizedLabel(name){{
            setVerticalTextPosition(CENTER);
        }});

        // Extension
        addToLeft(extension = new AutoSizedLabel(includeElement.getExtension()){{
            setVerticalTextPosition(CENTER);
            setForeground(UIUtil.getInactiveTextColor());
        }});

        // nearClass
        if(element.getNearClass() != null) {
            addToRight(nearClass = new JLabel(element.getNearClass()) {{
                setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
                setForeground(UIUtil.getInactiveTextColor());
                setVerticalTextPosition(CENTER);
            }});
        }
    }

    public boolean haveErrors() {
        File file = new File(includeElement.getResourcePath()  + "/" + includeElement.getPath());
        if(!file.exists()) {
            setFileTypeIcon();
            return true;
        }

        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPathIfCached(includeElement.getResourcePath() + "/" + includeElement.getPath());
        if (virtualFile == null) {
            setFileTypeIcon();
            return true;
        }

        PsiFile psiFile = PsiManager.getInstance(includeElement.getProject()).findFile(virtualFile);
        if (psiFile == null) {
            setFileTypeIcon();
            return true;
        }

        ApplicationManager.getApplication().invokeLater(() -> icon.setIcon(psiFile.getIcon(0)));

        return false;
    }

    public void setFileTypeIcon(){
        Icon fileIcon = FileTypeManager.getInstance().getFileTypeByFileName(includeElement.getPath()).getIcon();
        ApplicationManager.getApplication().invokeLater(() -> icon.setIcon(fileIcon));
    }

    public void onError(boolean haveError) {
        hasError = haveError;
        ApplicationManager.getApplication().invokeLater(this::updateColors);
    }

    public void updateColors(){
        path.setForeground(getTextColor());

        passive_path.setForeground(getAlternativeTextColor());
        if(nearClass != null)
            nearClass.setForeground(getAlternativeTextColor());
        extension.setForeground(getAlternativeTextColor());

        super.updateColors();
    }

    public Color getBackgroundColor() {
        if(getState() == UNSELECTED && hasError)
            return getErrorColor();
        else
            return super.getBackgroundColor();
    }

    public void setResourcePath(String resource_path){
        includeElement.setResourcePath(resource_path);
    }

    public IncludeElement getIncludeElement(){
        return includeElement;
    }

    public String toString() {
        return "IncludeListElement{" +
                "text=" + includeElement.getPath() +
                ", hasError=" + hasError +
                ", dropHovered=" + isDropHovered() +
                ", dropSide=" + getDropSide() +
                '}';
    }
}
