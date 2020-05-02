package com.husker.weblafplugin.parameters;

import com.husker.weblafplugin.tools.Tools;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;

public class ImageChooserParameter extends ResourceChooserParameter{

    public ImageChooserParameter(String name, int width) {

        super(name, Tools.getImageFileType(), width);
    }

    public ImageChooserParameter(String name) {
       this(name, DEFAULT_WIDTH);
    }

    public boolean haveErrors() {
        if(getText().equals(""))
            return false;
        return super.haveErrors();
    }

}
