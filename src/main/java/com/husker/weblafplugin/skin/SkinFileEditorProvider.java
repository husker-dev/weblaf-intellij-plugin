package com.husker.weblafplugin.skin;

import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class SkinFileEditorProvider implements FileEditorProvider {
    private static final String EDITOR_TYPE_ID = "weblaf_skin";

    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        if(file.getExtension() == null)
            return false;
        return file.getExtension().equals("xml");
    }

    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        return new SkinFileEditor(project, file);
    }

    public String getEditorTypeId() {
        return EDITOR_TYPE_ID;
    }

    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR;
    }

}
