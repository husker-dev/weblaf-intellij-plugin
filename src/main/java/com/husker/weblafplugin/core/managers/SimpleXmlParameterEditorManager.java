package com.husker.weblafplugin.core.managers;

import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;

import java.awt.*;

public class SimpleXmlParameterEditorManager {

    public static SimpleXmlParameterEditor getByParameterContext(Component context){
        Component current_parent = context.getParent();

        while(true){
            if(current_parent instanceof SimpleXmlParameterEditor)
                return (SimpleXmlParameterEditor) current_parent;
            else
                current_parent = current_parent.getParent();
        }
    }
}
