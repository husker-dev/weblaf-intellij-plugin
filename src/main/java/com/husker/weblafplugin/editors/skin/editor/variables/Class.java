package com.husker.weblafplugin.editors.skin.editor.variables;

import com.husker.weblafplugin.editors.skin.editor.SkinEditorUI;
import com.husker.weblafplugin.variables.SimpleXmlVariable;

public class Class extends SimpleXmlVariable {
    public Class(SkinEditorUI editor) {
        super(editor, "class");
    }

    public void setValue(Object value) {
        super.setValue(value);
        getEditor().setClassName(value.toString());
    }
}
