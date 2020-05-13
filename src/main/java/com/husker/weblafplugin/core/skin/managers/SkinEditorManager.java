package com.husker.weblafplugin.core.skin.managers;


import com.husker.weblafplugin.core.skin.AbstractSkinEditor;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SkinEditorManager {

    private static final HashMap<Component, AbstractSkinEditor> cached = new HashMap<>();

    public static AbstractSkinEditor get(Component context){
        if(cached.containsKey(context))
            return cached.get(context);

        Component current_parent = context.getParent();

        while(true){
            if(current_parent instanceof AbstractSkinEditor) {
                cached.put(context, (AbstractSkinEditor) current_parent);
                return (AbstractSkinEditor) current_parent;
            }else
                current_parent = current_parent.getParent();
        }
    }

    public static void dispose(AbstractSkinEditor editor){
        Iterator<Map.Entry<Component, AbstractSkinEditor>> iterator = cached.entrySet().iterator();
        iterator.forEachRemaining(entry -> {
            if(entry.getValue().equals(editor))
                iterator.remove();
        });
    }
}
