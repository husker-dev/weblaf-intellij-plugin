package com.husker.weblafplugin.skin.core.parameters;

import com.husker.weblafplugin.skin.core.IncludeElement;
import com.husker.weblafplugin.skin.core.components.control.IncludeListControl;
import com.husker.weblafplugin.skin.core.components.list.include.IncludeList;
import com.husker.weblafplugin.skin.core.dialogs.IncludeElementEditorDialog;
import com.intellij.openapi.project.Project;

public class IncludeListParameter extends FileListParameter<IncludeElement> {

    protected IncludeList list;

    public IncludeListParameter(String name) {
        super(name);
    }

    public void onInit(){
        Project project = getSkinEditor().getProject();
        setListControl(new IncludeListControl(list = new IncludeList(), () -> {
            return new IncludeElementEditorDialog(project, getSkinEditor().getClassName()).getIncludeElement();
        }));
    }

    public boolean haveErrors() {
        list.setResourcePath(getSkinEditor().getResourcePath());
        return false;
    }

}
