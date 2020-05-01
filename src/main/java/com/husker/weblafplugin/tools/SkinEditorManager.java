package com.husker.weblafplugin.tools;

import com.husker.weblafplugin.editors.skin.editor.SkinEditor;

import java.awt.*;

public class SkinEditorManager {

    public static SkinEditor get(Component context){
        Component current_parent = context.getParent();

        while(true){
            if(current_parent instanceof SkinEditor)
                return (SkinEditor) current_parent;
            else
                current_parent = current_parent.getParent();
        }
    }
}
