package com.husker.weblafplugin.skin.parameters;

import com.husker.weblafplugin.core.skin.components.list.classes.ClassList;
import com.husker.weblafplugin.core.skin.components.control.ClassListControl;
import com.husker.weblafplugin.core.skin.parameters.FileListParameter;
import com.intellij.openapi.project.Project;

public class ClassListParameter extends FileListParameter<String> {

    public ClassListParameter(String name) {
        super(name);
    }

    public void onInit(){
        Project project = getSkinEditor().getProject();
        setListControl(new ClassListControl(project, new ClassList(project)));
    }
}
