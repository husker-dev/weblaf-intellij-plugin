package com.husker.weblafplugin.skin.core.managers;


import com.husker.weblafplugin.skin.core.SkinEditor;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SkinEditorManager {

    private static final HashMap<Component, SkinEditor> cached = new HashMap<>();

    public static SkinEditor get(Component context){
        if(cached.containsKey(context))
            return cached.get(context);

        Component current_parent = context.getParent();

        while(true){
            if(current_parent instanceof SkinEditor) {
                cached.put(context, (SkinEditor) current_parent);
                return (SkinEditor) current_parent;
            }else
                current_parent = current_parent.getParent();
        }
    }

    public static void dispose(SkinEditor editor){
        Iterator<Map.Entry<Component, SkinEditor>> iterator = cached.entrySet().iterator();
        iterator.forEachRemaining(entry -> {
            if(entry.getValue().equals(editor))
                iterator.remove();
        });
    }
}
