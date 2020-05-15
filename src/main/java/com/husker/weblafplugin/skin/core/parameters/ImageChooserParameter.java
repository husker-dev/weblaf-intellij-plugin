package com.husker.weblafplugin.skin.core.parameters;

import com.husker.weblafplugin.core.components.textfield.magic.path.PathMagicContent;
import com.husker.weblafplugin.core.tools.Tools;

public class ImageChooserParameter extends ResourceChooserParameter {

    public ImageChooserParameter(String name, int width) {
        super(name, Tools.getImageFileType(), width);
    }

    public void onInit() {
        super.onInit();

        textField.setMagicPanel(new PathMagicContent());
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
