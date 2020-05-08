package com.husker.weblafplugin.tools;

import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class Listeners {

    public static void selectedFileEditorChanged(Project project, Consumer<FileEditorManagerEvent> listener){
        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                listener.accept(event);
            }
        });
    }

    public static void afterFileSystemChanges(Project project, Consumer<List<? extends VFileEvent>> listener){
        project.getMessageBus().connect().subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
            public void after(@NotNull List<? extends VFileEvent> events) {
                listener.accept(events);
            }
        });
    }
}
