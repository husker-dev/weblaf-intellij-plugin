package com.husker.weblafplugin.skin.core.components.list;

import com.husker.weblafplugin.core.components.list.DefaultCellRenderer;
import com.husker.weblafplugin.core.tools.Tools;
import com.husker.weblafplugin.skin.core.IncludeElement;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.util.PsiUtilCore;

import javax.swing.*;

public class IncludeListCellRenderer extends DefaultCellRenderer<IncludeElement> {

    public void initComponents(){
        addIcon("icon");
        addLabel("folder", false);
        addLabel("name");
        addLabel("extension", false);
        addLabelToRight("nearClass", false);
        addIconToRight("module");
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

    private Icon getModuleIcon(){
        if(getElement().getNearClass() != null || getElement().getFullPath().contains(".jar")) {
            PsiClass psiClass;
            if (getElement().getNearClass() == null)
                psiClass = ((IncludeList) getList()).getSkinEditor().Resources.getPsiClass();
            else
                psiClass = Tools.getClassByPath(((IncludeList) getList()).getSkinEditor().getProject(), getElement().getNearClass());
            return Tools.getModuleIcon(psiClass);
        }
        return null;
    }


}
