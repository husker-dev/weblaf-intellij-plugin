package com.husker.weblafplugin.core.editor;

import com.husker.weblafplugin.core.WLF_TypeChecker;
import com.husker.weblafplugin.core.managers.ParameterManager;
import com.husker.weblafplugin.core.tools.Tools;
import com.husker.weblafplugin.core.tools.XmlTools;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;

public abstract class SimpleXmlParameterEditor extends WLF_Editor{

    private Element rootHead;
    private Element oldFile;
    private int fileFormattingType = WLF_TypeChecker.SKIN;

    public SimpleXmlParameterEditor(Project project, VirtualFile file) {
        super(project, file);

        setLayout(new VerticalFlowLayout());
        ParameterManager.markAsParameterContainer(this);
    }

    public void onUpdate() {
        reloadRootElement();

        // If xml changed
        if (oldFile == null || !XmlTools.areEqual(rootHead, oldFile))
            ParameterManager.reloadVariables(this);

        oldFile = rootHead;
    }

    public Element getRootElement(){
        return rootHead;
    }

    public void reloadRootElement(){
        try {
            rootHead = XmlTools.getElement(Tools.getPsi(getProject(), getVirtualFile()).getText());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setFileFormattingType(int fileType){
        this.fileFormattingType = fileType;
    }

    public void setRootElement(Element element){
        Tools.writeText(getVirtualFile(), XmlTools.formatElement(element, fileFormattingType));

        rootHead = element;
        oldFile = rootHead;
    }

    public void dispose() {
        ParameterManager.disposeContainer(this);
    }
}
