package com.husker.weblafplugin.skin;

import com.husker.weblafplugin.skin.managers.ParameterManager;
import com.husker.weblafplugin.core.tools.Listeners;
import com.husker.weblafplugin.skin.managers.SkinEditorManager;
import com.husker.weblafplugin.core.tools.Tools;
import com.husker.weblafplugin.core.tools.XmlTools;
import com.intellij.javaee.ExternalResourceManager;
import com.intellij.openapi.application.ApplicationManager;
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
import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class SkinEditor extends JPanel {

    private Project project;
    private Element skin_head;
    private VirtualFile file;

    private Element old_file;

    private final Consumer<FileEditorManagerEvent> selectedFileEditorChangedListener;

    public Resources Resources = new Resources();

    public SkinEditor(Project project, VirtualFile file){
        this.project = project;
        this.file = file;
        setLayout(new VerticalFlowLayout());

        // TODO: move to plugin init action!!!
        ApplicationManager.getApplication().runWriteAction(() -> {
            ExternalResourceManager.getInstance().addResource("http://weblookandfeel.com/XmlSkin", "validation/XmlSkin.xsd");
            ExternalResourceManager.getInstance().addResource("http://weblookandfeel.com/XmlSkinExtension", "validation/XmlSkinExtension.xsd");
        });

        ParameterManager.markAsParameterContainer(this);

        // File editor changed listener
        selectedFileEditorChangedListener = event -> {
            try {
                if (event.getNewEditor() != null && event.getNewEditor().getClass() == SkinFileEditor.class) {
                    try {
                        reloadSkinElement();

                        // If xml changed
                        if (old_file == null || !XmlTools.areEqual(skin_head, old_file))
                            ParameterManager.reloadVariables(this);

                        old_file = skin_head;
                    } catch (NullPointerException np) {
                        // Ignore
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // TODO Make error screen (or not...)
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        };
        Listeners.selectedFileEditorChanged(project, selectedFileEditorChangedListener);

        reloadSkinElement();
    }

    public Element getSkinElement(){
        return skin_head;
    }

    public Project getProject(){
        return project;
    }

    public void reloadSkinElement(){
        try {
            skin_head = XmlTools.getElement(Tools.getPsi(project, file).getText());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setSkinElement(Element element){
        Tools.writeText(file, XmlTools.formatElement(element));

        skin_head = element;
        old_file = skin_head;
    }

    public class Resources {
        public String getClassPath(){
            return getPsiClass().getQualifiedName();
        }

        public PsiClass getPsiClass(){
            try {
                PsiClass clazz = SkinEditor.this.getPsiClass();
                if(clazz == null)
                    return null;

                if(clazz.getConstructors().length == 0)
                    return null;

                if(clazz.getConstructors()[0].getBody() == null)
                    clazz = (PsiClass) clazz.getNavigationElement();

                if(clazz.getConstructors()[0].getBody() == null)
                    return getCompiledClassResourcePath(clazz);

                PsiExpressionStatementImpl constructor = (PsiExpressionStatementImpl) clazz.getConstructors()[0].getBody().getStatements()[0];

                PsiMethodCallExpression super_call = (PsiMethodCallExpression)constructor.getExpression();
                PsiNewExpression resource_instance = (PsiNewExpression)super_call.getArgumentList().getExpressions()[0];

                String resource_class_name = resource_instance.getClassOrAnonymousClassReference().getQualifiedName();

                if(resource_class_name.equals("com.alee.api.resource.ClassResource")){
                    PsiClassObjectAccessExpressionImpl class_resource_class_expression = (PsiClassObjectAccessExpressionImpl) resource_instance.getArgumentList().getExpressions()[0];

                    PsiTypeElement class_resource_class_type = (PsiTypeElement) class_resource_class_expression.findChildByRole(ChildRole.TYPE);
                    PsiJavaCodeReferenceElementImpl class_resource_class_reference = (PsiJavaCodeReferenceElementImpl) class_resource_class_type.getChildren()[0];

                    return Tools.getClassByPath(project, class_resource_class_reference.getCanonicalText());
                }
                return null;
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }

        public String getResourcePath(){
            return Tools.getClassResourcePath(getPsiClass());
        }
    }

    private PsiClass getCompiledClassResourcePath(PsiClass clazz){
        PsiJavaFile file = (PsiJavaFile) clazz.getContainingFile();
        String text = file.getText();

        String class_name = text.split("ClassResource\\(")[1].split("\\.class")[0];

        ArrayList<String> folders = new ArrayList<>();
        folders.add(text.split("package")[1].split(";")[0].trim());

        // Getting all package imports
        String[] import_split = text.split("import");
        for(int i = 1; i < import_split.length; i++) {
            String import_text = import_split[i].split(";")[0].trim();
            if(import_text.endsWith(".*"))
                folders.add(import_text);
        }

        // Testing for single class import
        for(int i = 1; i < import_split.length; i++) {
            String import_text = import_split[i].split(";")[0].trim();
            if(import_text.split("\\.")[import_text.split("\\.").length - 1].equals(class_name))
                return Tools.getClassByPath(project, import_text);
        }
        // Testing folders
        for(String folder : folders){
            PsiClass found;
            if(folder.contains(".*"))
                found = Tools.getClassByPath(project, folder.replace("*", class_name));
            else
                found = Tools.getClassByPath(project, folder + "." + class_name);
            if(found != null)
                return found;
        }
        // Testing class_name itself
        return Tools.getClassByPath(project, class_name);
    }

    public PsiClass getPsiClass(){
        return Tools.getClassByPath(project, getClassPath());
    }

    public String getClassPath(){
        return getSkinElement().getChildText("class", getSkinElement().getNamespace());
    }

    public void dispose(){
        ParameterManager.disposeContainer(this);
        SkinEditorManager.dispose(this);
    }

}