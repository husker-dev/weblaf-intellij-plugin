package com.husker.weblafplugin.core.components.list.classes;

import com.husker.weblafplugin.core.components.list.FileCellRenderer;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.psi.PsiClass;

public class ClassListCellRenderer extends FileCellRenderer<String> {

    public void initComponents() {
        addIcon("icon");
        addLabel("name");
        addLabel("path", false);
        //addLabelToRight("library", false);
        addIconToRight("module");
    }

    public void updateContent() {
        PsiClass psiClass = ((ClassList)getList()).getPsiClass(getElement());

        if(psiClass != null) {
            setIcon("icon", psiClass.getIcon(0));
            setLabelText("name", psiClass.getName());
            setLabelText("path", getElement().contains(".") ? " (" + getElement().substring(0, getElement().lastIndexOf(".")) + ")" : "");
            setIcon("module", Tools.getModuleIcon(psiClass));
        }else{
            setIcon("icon", JavaClassFileType.INSTANCE.getIcon());
            setLabelText("name", getElement());
            setLabelText("path", null);
            setIcon("module", null);
        }
    }

    public String getFilePath() {
        PsiClass psiClass = ((ClassList)getList()).getPsiClass(getElement());
        if(psiClass == null)
            return "";
        return psiClass.getContainingFile().getVirtualFile().getPath();
    }

    public boolean haveError() {
        return !((ClassList)getList()).isExist(getElement());
    }
}
