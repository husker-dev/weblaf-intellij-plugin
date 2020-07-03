package com.husker.weblafplugin.core.components.list.classes;

import com.husker.weblafplugin.core.components.list.FileList;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;


public class ClassList extends FileList<String> {

    public ClassList(Project project){
        super(project);
        setCellRenderer(new ClassListCellRenderer());
        setDragEnabled(true);
    }

    protected void updateCachedData() {
        for(String classPath : getContent()) {
            PsiClass clazz = Tools.getClassByPath(getProject(), classPath);
            cache("psiClasses", classPath, clazz);
            cache("error", classPath, clazz == null);
        }
    }

    protected boolean haveError(String element) {
        return false;
    }

    public boolean isExist(String classPath){
        return getCached("psiClasses", classPath) != null;
    }

    public PsiClass getPsiClass(String path){
        return (PsiClass) getCached("psiClasses", path);
    }
}
