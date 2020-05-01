package com.husker.weblafplugin.editors.skin.editor.variables;

import com.husker.weblafplugin.editors.skin.editor.SkinEditorUI;
import com.husker.weblafplugin.variables.Variable;

public class ResourcePath extends Variable {

    private SkinEditorUI editor;

    public ResourcePath(SkinEditorUI editor) {
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
