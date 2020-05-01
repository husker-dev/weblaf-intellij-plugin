package com.husker.weblafplugin.parameters;

import com.husker.weblafplugin.dialogs.FileListDialog;
import com.husker.weblafplugin.tools.SkinEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;

import java.io.File;


public class ResourceChooserParameter extends TextButtonParameter {


    public ResourceChooserParameter(String name, FileType fileType, int width) {
        super(name, "...", width);

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
            return !new File(getSkinEditor().getResourcePath().replace("\\", "/") + "/" + getFilePath()).exists();
        }catch (Exception ex){
            return true;
        }
    }
}
