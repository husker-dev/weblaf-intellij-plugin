package com.husker.weblafplugin.style;

import com.husker.weblafplugin.core.WLF_IconProvider;
import com.husker.weblafplugin.core.editor.WLF_Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBTabbedPane;
import icons.MyFileIcons;

import javax.swing.*;
import java.util.List;

public class StyleEditor extends WLF_Editor {

    public StyleEditor(Project project, VirtualFile file) {
        super(project, file);
    }

    public String getTitle() {
        return "Style editor";
    }

    public void onUpdate() {

    }

    public int getPreferredTabIndex(WLF_Editor[] list) {
        return 5;
    }

    public Icon getIcon() {
        return MyFileIcons.STYLE;
    }

    public void dispose() {

    }
}
