package com.husker.weblafplugin.skin.core.components.list.include;

import com.husker.weblafplugin.core.components.list.files.AbstractFileList;
import com.husker.weblafplugin.skin.core.IncludeElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class IncludeList extends AbstractFileList<IncludeElement> {

    public IncludeList(){
        setListElementGenerator(IncludeListElement::new);
    }

    public void setResourcePath(String resource_path){
        for(Component component : getComponents()) {
            if (component instanceof IncludeListElement) {
                IncludeListElement element = (IncludeListElement) component;
                element.setResourcePath(resource_path);
            }
        }
        testForExistence();
    }
}
