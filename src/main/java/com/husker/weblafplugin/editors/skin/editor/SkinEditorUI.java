package com.husker.weblafplugin.editors.skin.editor;

import com.husker.weblafplugin.editors.skin.SkinFileEditor;
import com.husker.weblafplugin.components.parameter.ParameterManager;
import com.husker.weblafplugin.tools.Listeners;
import com.husker.weblafplugin.tools.Tools;
import com.husker.weblafplugin.tools.XmlTools;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaCodeReferenceElementImpl;
import com.intellij.psi.impl.source.tree.ChildRole;
import com.intellij.psi.impl.source.tree.java.PsiClassObjectAccessExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiExpressionStatementImpl;
import org.jdom.Element;

import javax.swing.*;
import java.io.File;
import java.util.function.Consumer;

public class SkinEditorUI extends JPanel {

    private Project project;
    private Element skin_head;
    private VirtualFile file;

    private Element old_file;

    private Consumer<FileEditorManagerEvent> selectedFileEditorChangedListener;

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
        selectedFileEditorChangedListener = event -> {
            if(event.getNewEditor() != null && event.getNewEditor().getClass() == SkinFileEditor.class){
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
        };
        Listeners.selectedFileEditorChanged(project, selectedFileEditorChangedListener);

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
        }catch (Exception ignored){}
    }

    public void setSkinElement(Element element){
        Tools.writeText(file, XmlTools.formatElement(element));

        skin_head = element;
        old_file = skin_head;
    }

    public String getResourcePath(){
        try {
            PsiExpressionStatementImpl constructor = (PsiExpressionStatementImpl) getPsiClass().getConstructors()[0].getBody().getStatements()[0];

            PsiMethodCallExpression super_call = (PsiMethodCallExpression)constructor.getExpression();
            PsiNewExpression resource_instance = (PsiNewExpression)super_call.getArgumentList().getExpressions()[0];

            String resource_class_name = resource_instance.getClassOrAnonymousClassReference().getQualifiedName();

            if(resource_class_name.equals("com.alee.api.resource.ClassResource")){
                PsiClassObjectAccessExpressionImpl class_resource_class_expression = (PsiClassObjectAccessExpressionImpl)resource_instance.getArgumentList().getExpressions()[0];

                PsiTypeElement class_resource_class_type = (PsiTypeElement) class_resource_class_expression.findChildByRole(ChildRole.TYPE);
                PsiJavaCodeReferenceElementImpl class_resource_class_reference = (PsiJavaCodeReferenceElementImpl) class_resource_class_type.getChildren()[0];

                PsiClass class_resource_class = Tools.getClassByPath(project, class_resource_class_reference.getCanonicalText());

                String class_path = class_resource_class.getContainingFile().getVirtualFile().getPath();
                return new File(class_path).getParent().replace("\\", "/");
            }
            return null;
        }catch (Exception ex){
            return null;
        }
    }

    public PsiClass getPsiClass(){
        return Tools.getClassByPath(project, getClassName());
    }

    public String getClassName(){
        return getSkinElement().getChildText("class", getSkinElement().getNamespace());
    }

    public void dispose(){
        ParameterManager.disposeContainer(this);
    }

}
