package com.husker.weblafplugin.skin.variables.impl;

import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.skin.SkinEditor;
import com.husker.weblafplugin.skin.variables.SimpleXmlVariable;

public class SupportedSystemVariable extends SimpleXmlVariable {

    public SupportedSystemVariable(SimpleXmlParameterEditor editor) {
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
