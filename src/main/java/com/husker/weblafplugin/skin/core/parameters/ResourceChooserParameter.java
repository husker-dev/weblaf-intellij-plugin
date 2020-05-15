package com.husker.weblafplugin.skin.core.parameters;

import com.husker.weblafplugin.core.dialogs.FileListDialog;
import com.husker.weblafplugin.skin.core.managers.SkinEditorManager;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;


public class ResourceChooserParameter extends TextButtonParameter {

    private FileType fileType;

    public ResourceChooserParameter(String name, FileType fileType, int width) {
        super(name, "...", width);
        this.fileType = fileType;
    }

    public void onInit(){
        super.onInit();

        addButtonListener(e -> {
            String resource_folder = getSkinEditor().getResourcePath();

            PsiFile psiFile = new FileListDialog(getSkinEditor().getProject(), fileType, resource_folder).getPsiFile();
            if(psiFile != null){
                String file = psiFile.getVirtualFile().getPath();
                if(resource_folder != null)
                    file = file.replaceAll(resource_folder, "").substring(1);
                setText(file);
            }
        });
    }

    public ResourceChooserParameter(String name, FileType fileType) {
        this(name, fileType, DEFAULT_WIDTH);
    }

    public String getResourcePath(){
        return SkinEditorManager.get(this).getResourcePath();
    }

    public String getFilePath(){
        return getText();
    }

    public boolean haveErrors() {
        try {
            String path = getSkinEditor().getResourcePath() + "/" + getFilePath();
            VirtualFile file = Tools.getVirtualFile(path);
            return file == null;
        }catch (Exception ex){
            return true;
        }
    }
}
