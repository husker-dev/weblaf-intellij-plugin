package com.husker.weblafplugin.core.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class WLF_FileEditorProvider implements FileEditorProvider {

    private static final String EDITOR_TYPE_ID = "weblaf";

    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        if(file.getExtension() == null)
            return false;

        return file.getExtension().equals("xml");
    }

    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        return new WLF_FileEditor(project, file);
    }

    public String getEditorTypeId() {
        return EDITOR_TYPE_ID;
    }

    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR;
    }
}
