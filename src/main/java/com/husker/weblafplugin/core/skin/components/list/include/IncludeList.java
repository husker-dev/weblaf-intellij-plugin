package com.husker.weblafplugin.core.skin.components.list.include;

import com.husker.weblafplugin.core.components.list.files.AbstractFileList;
import com.husker.weblafplugin.core.skin.IncludeElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class IncludeList extends AbstractFileList<IncludeElement> {

    public IncludeList(){
        setListElementGenerator(IncludeListElement::new);
    }

    public void setContent(IncludeElement[] content) {
        if(content != null && content.length > 0) {

            // Folder path
            ArrayList<String> folders = new ArrayList<>(Arrays.asList(content[0].getPath().split("/")));

            // Remove file from path
            folders.remove(folders.size() - 1);

            for (int i = 0; i < folders.size(); i++) {
                String path = String.join("/", folders) + "/";

                boolean hasSame = true;
                for (IncludeElement element : content) {
                    if (!element.getPath().startsWith(path)) {
                        hasSame = false;
                        break;
                    }
                }
                if (hasSame) {
                    for (IncludeElement element : content)
                        element.setPassivePath(path);
                    break;
                }
            }
        }

        super.setContent(content);
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
