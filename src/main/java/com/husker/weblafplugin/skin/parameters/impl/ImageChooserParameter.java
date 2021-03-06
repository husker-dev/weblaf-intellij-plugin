package com.husker.weblafplugin.skin.parameters.impl;

import com.husker.weblafplugin.core.components.textfield.magic.impl.MagicPathContent;
import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.core.managers.SimpleXmlParameterEditorManager;
import com.husker.weblafplugin.core.tools.Tools;
import com.husker.weblafplugin.skin.parameters.impl.ResourceChooserParameter;

public class ImageChooserParameter extends ResourceChooserParameter {

    public ImageChooserParameter(String name, int width) {
        super(name, Tools.getImageFileType(), width);
    }

    public void onInit() {
        super.onInit();

        SimpleXmlParameterEditor editor = SimpleXmlParameterEditorManager.getByParameterContext(this);
        textField.setMagicPanel(new MagicPathContent(editor.getProject()));
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
