package com.husker.weblafplugin.core.skin.components.list.include;

import com.husker.weblafplugin.core.components.AutoSizedLabel;
import com.husker.weblafplugin.core.components.list.List;
import com.husker.weblafplugin.core.components.list.files.AbstractFileListElement;
import com.husker.weblafplugin.core.skin.IncludeElement;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.ide.ui.EditorOptionsTopHitProvider;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.impl.local.LocalFileSystemImpl;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class IncludeListElement extends AbstractFileListElement<IncludeElement> {

    protected AutoSizedLabel passive_path, path, extension;
    protected JLabel nearClass;

    public IncludeListElement(IncludeElement element){
        super(element);
    }

    public void onNameLabelsInit() {
        IncludeElement element = getContent();

        // Passive path
        addToLeft(passive_path = new AutoSizedLabel(element.getPassivePath()){{
            setVerticalTextPosition(CENTER);
            setForeground(UIUtil.getInactiveTextColor());
        }});

        // Path
        String name = element.getPath();
        name = name.replaceFirst(element.getPassivePath(), "");
        if(name.contains("."))
            name = name.substring(0, name.lastIndexOf("."));
        addToLeft(path = new AutoSizedLabel(name){{
            setVerticalTextPosition(CENTER);
        }});

        // Extension
        addToLeft(extension = new AutoSizedLabel(element.getExtension()){{
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

    public void updateColors(){
        path.setForeground(getTextColor());

        passive_path.setForeground(getAlternativeTextColor());
        if(nearClass != null)
            nearClass.setForeground(getAlternativeTextColor());
        extension.setForeground(getAlternativeTextColor());

        super.updateColors();
    }

    public Color getBackgroundColor() {
        if(getState() == List.ElementState.UNSELECTED && !hasError){

            if((getContent().getResourcePath() + "/" + getContent().getPath()).contains(".jar!"))
                return new JBColor(new Color(255, 255, 228), new Color(79, 75, 65));
        }

        return super.getBackgroundColor();
    }

    public boolean testForExistence() {
        IncludeElement element = getContent();

        String file_path = element.getResourcePath() + "/" + element.getPath();

        file_path = file_path.replace("\\", "/");

        try {
            VirtualFile virtualFile = Tools.getVirtualFile(file_path);

            if (virtualFile == null) {
                setIcon(FileTypeManager.getInstance().getFileTypeByFileName(element.getPath()).getIcon());
                return true;
            }

            PsiFile psiFile = PsiManager.getInstance(element.getProject()).findFile(virtualFile);
            if (psiFile == null) {
                setIcon(FileTypeManager.getInstance().getFileTypeByFileName(element.getPath()).getIcon());
                return true;
            }
            setIcon(psiFile.getIcon(0));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return false;
    }


    public void setResourcePath(String resource_path){
        getContent().setResourcePath(resource_path);
    }

    public String toString() {
        return "IncludeListElement{" +
                "text=" + getContent().getPath() +
                ", hasError=" + hasError +
                ", dropHovered=" + isDropHovered() +
                ", dropSide=" + getDropSide() +
                '}';
    }
}
