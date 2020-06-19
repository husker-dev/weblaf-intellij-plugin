package com.husker.weblafplugin.skin.variables.impl;

import com.husker.weblafplugin.skin.SkinEditor;
import com.husker.weblafplugin.skin.variables.Variable;

public class ResourcePathVariable extends Variable {

    private SkinEditor editor;

    public ResourcePathVariable(SkinEditor editor) {
        this.editor = editor;
    }

    public void setValue(Object object) {

    }

    public Object getValue() {
        if(editor.Resources.getResourcePath() != null)
            return editor.Resources.getResourcePath().replace("/", "\\");
        else
            return "[Wrong class]";
    }
}
