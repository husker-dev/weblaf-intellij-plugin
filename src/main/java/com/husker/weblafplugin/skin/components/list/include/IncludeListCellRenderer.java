package com.husker.weblafplugin.skin.components.list.include;

import com.husker.weblafplugin.core.components.list.FileCellRenderer;
import com.husker.weblafplugin.core.tools.Tools;
import com.husker.weblafplugin.skin.include.IncludeElement;
import com.husker.weblafplugin.skin.managers.SkinEditorManager;
import com.intellij.psi.PsiClass;

import javax.swing.*;

public class IncludeListCellRenderer extends FileCellRenderer<IncludeElement> {

    public void initComponents(){
        addIcon("icon");
        addLabel("folder", false);
        addLabel("name", true, false);
        addLabel("extension", false, false);

        addIconToRight("module");
        addLabelToRight("nearClass", false);
    }
    public void updateContent() {
        setIcon("icon", ((IncludeList)getList()).getIcon(getElement()));
        setIcon("module", getModuleIcon());

        setLabelText("folder", getElement().getFolderPath());
        setLabelText("name", getElement().getName());
        setLabelText("extension", getElement().getExtension());
        setLabelText("nearClass", getElement().getNearClass() == null ? null : "(" + getElement().getNearClass() + ")");
    }

    public boolean haveError() {
        return !((IncludeList)getList()).isExist(getElement());
    }

    public String getFilePath() {
        return getElement().getFullPath();
    }

    public Icon getModuleIcon(){
        if(getElement().getNearClass() != null || getElement().getFullPath().contains(".jar")) {
            PsiClass psiClass;
            if (getElement().getNearClass() == null)
                psiClass = SkinEditorManager.Resources.getResourcePsiClass(((IncludeList) getList()).getSkinEditor());
            else
                psiClass = Tools.getClassByPath(((IncludeList) getList()).getSkinEditor().getProject(), getElement().getNearClass());
            return Tools.getModuleIcon(psiClass);
        }
        return null;
    }

}
