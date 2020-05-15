package com.husker.weblafplugin.skin.impl.variables;

import com.husker.weblafplugin.skin.core.SkinEditor;
import com.husker.weblafplugin.skin.core.variables.SimpleXmlVariable;

public class SupportedSystemVariable extends SimpleXmlVariable {

    public SupportedSystemVariable(SkinEditor editor) {
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
