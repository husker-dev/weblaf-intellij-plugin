package com.husker.weblafplugin.editors.skin;

import com.husker.weblafplugin.tools.WebLaFTypeChecker;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;

public class SkinFileEditorProvider implements FileEditorProvider {
    private static final String EDITOR_TYPE_ID = "weblaf_skin";

    public boolean accept(Project project, VirtualFile file) {
        if(file.getExtension().equals("xml")) {
            return true;
            //int type = WebLaFTypeChecker.getType(PsiManager.getInstance(project).findFile(file).getText());
            //return (type == WebLaFTypeChecker.SKIN) || (type == WebLaFTypeChecker.SKIN_AND_STYLE);
        }

        return false;
    }

    public FileEditor createEditor(Project project, VirtualFile file) {
        return new SkinFileEditor(project, file);
    }

    public String getEditorTypeId() {
        return EDITOR_TYPE_ID;
    }

    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR;
    }
}
