package com.husker.weblafplugin.skin.variables;

import com.husker.weblafplugin.core.skin.AbstractSkinEditor;
import com.husker.weblafplugin.core.skin.variables.Variable;

public class ResourcePathVariable extends Variable {

    private AbstractSkinEditor editor;

    public ResourcePathVariable(AbstractSkinEditor editor) {
        this.editor = editor;
    }

    public void setValue(Object object) {

    }

    public Object getValue() {
        if(editor.getResourcePath() != null)
            return editor.getResourcePath().replace("/", "\\");
        else
            return "[Wrong class]";
    }
}
