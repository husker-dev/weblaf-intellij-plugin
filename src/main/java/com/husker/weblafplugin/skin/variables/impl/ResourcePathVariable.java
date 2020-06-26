package com.husker.weblafplugin.skin.variables.impl;

import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.skin.SkinEditor;
import com.husker.weblafplugin.skin.managers.SkinEditorManager;
import com.husker.weblafplugin.skin.variables.Variable;

public class ResourcePathVariable extends Variable {

    private SimpleXmlParameterEditor editor;

    public ResourcePathVariable(SimpleXmlParameterEditor editor) {
        this.editor = editor;
    }

    public void setValue(Object object) {

    }

    public Object getValue() {
        String resourcePath = SkinEditorManager.Resources.getResourcePath(editor);
        if(resourcePath != null)
            return resourcePath.replace("/", "\\");
        else
            return "[Wrong class]";
    }
}
