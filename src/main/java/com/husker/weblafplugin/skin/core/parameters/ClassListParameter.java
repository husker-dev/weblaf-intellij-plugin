package com.husker.weblafplugin.skin.core.parameters;

import com.husker.weblafplugin.core.components.list.classes.ClassList;
import com.husker.weblafplugin.skin.core.components.control.ClassListControl;
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
