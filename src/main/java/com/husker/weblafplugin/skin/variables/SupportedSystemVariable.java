package com.husker.weblafplugin.skin.variables;

import com.husker.weblafplugin.core.skin.AbstractSkinEditor;
import com.husker.weblafplugin.core.skin.variables.SimpleXmlVariable;

public class SupportedSystemVariable extends SimpleXmlVariable {

    public SupportedSystemVariable(AbstractSkinEditor editor) {
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
