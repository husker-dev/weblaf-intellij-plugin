package com.husker.weblafplugin.skin.parameters;

import com.husker.weblafplugin.core.skin.IncludeElement;
import com.husker.weblafplugin.core.skin.components.control.IncludeListControl;
import com.husker.weblafplugin.core.skin.components.list.include.IncludeList;
import com.husker.weblafplugin.core.skin.parameters.FileListParameter;

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
