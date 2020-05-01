package com.husker.weblafplugin.editors.skin.editor.variables;

import com.husker.weblafplugin.editors.skin.editor.SkinEditorUI;
import com.husker.weblafplugin.variables.SimpleXmlVariable;

public class SupportedSystem extends SimpleXmlVariable {

    public SupportedSystem(SkinEditorUI editor) {
        super(editor, "supportedSystems");
    }

    public Object getValue() {
        Object value = super.getValue();
        if(value == null)
            return null;

        return value.toString().split(",");
    }


    public void setValue(Object value) {
        super.setValue(String.join(",", (String[])value));
    }
}
