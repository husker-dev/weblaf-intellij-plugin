package com.husker.weblafplugin.parameters;

import org.intellij.images.fileTypes.impl.ImageFileType;

public class ImageChooserParameter extends ResourceChooserParameter{

    public ImageChooserParameter(String name, int width) {
        super(name, ImageFileType.INSTANCE, width);
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
