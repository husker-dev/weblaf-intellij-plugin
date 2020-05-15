package com.husker.weblafplugin.core.components.list.classes;

import com.husker.weblafplugin.core.components.list.files.AbstractFileList;
import com.intellij.openapi.project.Project;

public class ClassList extends AbstractFileList<String> {

    protected Project project;

    public ClassList(Project project){
        setListElementGenerator(content -> new ClassListElement(project, content));
    }

    public Project getProject(){
        return project;
    }
}
