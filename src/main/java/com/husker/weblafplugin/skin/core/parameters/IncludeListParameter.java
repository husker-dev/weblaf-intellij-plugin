package com.husker.weblafplugin.skin.core.parameters;

import com.husker.weblafplugin.skin.core.IncludeElement;
import com.husker.weblafplugin.skin.core.components.control.IncludeListControl;
import com.husker.weblafplugin.skin.core.components.list.include.IncludeList;

public class IncludeListParameter extends FileListParameter<IncludeElement> {

    protected IncludeList list;

    public IncludeListParameter(String name) {
        super(name);
    }

    public void onInit(){
        setListControl(new IncludeListControl(list = new IncludeList(), () -> null));
    }

    public boolean haveErrors() {
        list.setResourcePath(getSkinEditor().getResourcePath());
        return false;
    }

}
