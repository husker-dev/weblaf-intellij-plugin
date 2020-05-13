package com.husker.weblafplugin.core.components.list.files;

import com.husker.weblafplugin.core.components.list.List;
import com.intellij.openapi.application.ApplicationManager;

import java.awt.*;

public class AbstractFileList<T> extends List<T> {

    public void setContent(T[] content) {
        super.setContent(content);
        testForExistence();
    }

    public void testForExistence(){
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            ApplicationManager.getApplication().runReadAction(() -> {
                for (Component component : getComponents()) {
                    if (component instanceof AbstractFileListElement) {
                        AbstractFileListElement element = (AbstractFileListElement) component;
                        element.onError(element.testForExistence());
                    }
                }
            });
        });
    }
}
