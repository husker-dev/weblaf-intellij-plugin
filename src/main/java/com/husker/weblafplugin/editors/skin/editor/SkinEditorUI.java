package com.husker.weblafplugin.editors.skin.editor;

import com.husker.weblafplugin.editors.skin.SkinFileEditor;
import com.husker.weblafplugin.components.parameter.ParameterManager;
import com.husker.weblafplugin.tools.Listeners;
import com.husker.weblafplugin.tools.Tools;
import com.husker.weblafplugin.tools.XmlTools;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.impl.source.PsiJavaCodeReferenceElementImpl;
import com.intellij.psi.impl.source.tree.ChildRole;
import com.intellij.psi.impl.source.tree.java.PsiClassObjectAccessExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiExpressionStatementImpl;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import javax.swing.*;
import java.io.File;

public class SkinEditorUI extends JPanel {

    private Project project;
    private Element skin_head;
    private VirtualFile file;

    private String class_name = "";

    private Element old_file;

    private boolean isAfterEditorChangedEvent = false;

    public SkinEditorUI(Project project, VirtualFile file){
        this.project = project;
        this.file = file;
        setLayout(new VerticalFlowLayout());

        ParameterManager.markAsParameterContainer(this);

        ParameterManager.addReloadListener(this, container -> {
            if(!isAfterEditorChangedEvent)
                updateSkinElement();
        });

        // File editor changed listener
        Listeners.selectedFileEditorChanged(project, event -> {
            if(event.getNewEditor().getClass() == SkinFileEditor.class){
                try {
                    isAfterEditorChangedEvent = true;
                    updateSkinElement();

                    // If xml changed
                    if(old_file == null || !XmlTools.areEqual(skin_head, old_file))
                        ParameterManager.reloadVariables(this);

                    old_file = skin_head;
                }catch (NullPointerException np){
                    // Ignore
                }catch (Exception ex){
                    ex.printStackTrace();
                    // TODO Make error screen
                }
            }
        });
    }

    public Element getSkinElement(){
        return skin_head;
    }

    public Project getProject(){
        return project;
    }

    public void updateSkinElement(){
        try {
            skin_head = XmlTools.getElement(Tools.getPsi(project, file).getText());
        }catch (Exception ex){}
    }

    public void setSkinElement(Element element){
        Tools.writeText(file, XmlTools.formatElement(element));

        skin_head = element;
        old_file = skin_head;
    }

    public PsiClass getPsiClassFromXmlSkin(PsiClass xmlSkin){
        try {
            PsiClass skinClass = xmlSkin;

            PsiExpressionStatementImpl test = (PsiExpressionStatementImpl) skinClass.getConstructors()[0].getBody().getStatements()[0];

            // super(new ClassResource(TestSkinClass.class, ""))
            PsiElement e1 = test.getChildren()[0];

            // (new ClassResource(TestSkinClass.class, ""))
            PsiElement e2 = e1.getChildren()[1];

            // new ClassResource(TestSkinClass.class, "")
            PsiElement e3 = e2.getChildren()[1];

            // (TestSkinClass.class, "")
            PsiElement e4 = e3.getChildren()[4];

            // TestSkinClass.class
            PsiClassObjectAccessExpressionImpl e5 = (PsiClassObjectAccessExpressionImpl) e4.getChildren()[1];

            // TestSkinClass
            PsiTypeElement e6 = (PsiTypeElement) e5.findChildByRole(ChildRole.TYPE);
            PsiJavaCodeReferenceElementImpl classReference = (PsiJavaCodeReferenceElementImpl) e6.getChildren()[0];

            //!!!
            return Tools.getClassByPath(project, classReference.getCanonicalText());
        }catch (Exception ex){
            return null;
        }
    }

    public PsiClass getPsiClass(){
        return Tools.getClassByPath(project, getClassName());
    }

    public String getResourcePath(){
        PsiClass found_class = getPsiClassFromXmlSkin(getPsiClass());

        if(found_class == null)
            return null;

        String path = found_class.getContainingFile().getVirtualFile().getPath();
        return new File(path).getParent().replace("\\", "/");
    }

    public void setClassName(String class_name){
        this.class_name = class_name;
    }
    public String getClassName(){
        return getSkinElement().getChildText("class", getSkinElement().getNamespace());
    }

}
