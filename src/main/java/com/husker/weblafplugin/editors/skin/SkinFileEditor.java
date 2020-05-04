package com.husker.weblafplugin.editors.skin;

import com.husker.weblafplugin.editors.skin.editor.SkinEditor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

public class SkinFileEditor implements FileEditor {

    private JBScrollPane panel;
    private SkinEditor editor;

    public SkinFileEditor(Project project, VirtualFile file) {
        panel = new JBScrollPane(editor = new SkinEditor(project, file));
    }

    @NotNull
    public JComponent getComponent() {
        return panel;
    }

    @Nullable
    public JComponent getPreferredFocusedComponent() {
        return null;
    }

    @NotNull
    public String getName() {
        return "Skin parameters";
    }

    public void setState(@NotNull FileEditorState state) {
    }

    public boolean isModified() {
        return false;
    }

    public boolean isValid() {
        return true;
    }

    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Nullable
    public FileEditorLocation getCurrentLocation() {
        return null;
    }

    public void dispose() {
        editor.dispose();
    }

    @Nullable
    public <T> T getUserData(@NotNull Key<T> key) {
        return null;
    }

    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {

    }
}
