package com.husker.weblafplugin.style;

import com.husker.weblafplugin.core.editor.WLF_Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import icons.MyFileIcons;

public class StyleEditor extends WLF_Editor {

    public StyleEditor(Project project, VirtualFile file) {
        super(project, file);

        setTitle("Style editor");
        setPreferredTabIndex(5);
        setIcon(MyFileIcons.STYLE);
    }

    public void onUpdate() {

    }

    public void dispose() {

    }
}
