package com.husker.weblafplugin.skin.managers;



import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.core.tools.Tools;
import com.husker.weblafplugin.skin.SkinEditor;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaCodeReferenceElementImpl;
import com.intellij.psi.impl.source.tree.ChildRole;
import com.intellij.psi.impl.source.tree.java.PsiClassObjectAccessExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiExpressionStatementImpl;

import java.awt.*;
import java.util.ArrayList;

public class SkinEditorManager {

    public static SkinEditor getByParameterContext(Component context){
        Component current_parent = context.getParent();

        while(true){
            if(current_parent instanceof SkinEditor)
                return (SkinEditor) current_parent;
            else
                current_parent = current_parent.getParent();
        }
    }

    public static class Resources {
        public static String getClassPath(SimpleXmlParameterEditor editor){
            return getResourcePsiClass(editor).getQualifiedName();
        }

        public static PsiClass getResourcePsiClass(SimpleXmlParameterEditor editor){
            return getResourcePsiClass(SkinEditorManager.getPsiClass(editor));
        }

        public static PsiClass getResourcePsiClass(PsiClass clazz){
            try {
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

                    return Tools.getClassByPath(clazz.getProject(), class_resource_class_reference.getCanonicalText());
                }
                return null;
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }

        public static String getResourcePath(SimpleXmlParameterEditor editor){
            return Tools.getClassResourcePath(getResourcePsiClass(editor));
        }
    }

    private static PsiClass getCompiledClassResourcePath(PsiClass clazz){
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
                return Tools.getClassByPath(clazz.getProject(), import_text);
        }
        // Testing folders
        for(String folder : folders){
            PsiClass found;
            if(folder.contains(".*"))
                found = Tools.getClassByPath(clazz.getProject(), folder.replace("*", class_name));
            else
                found = Tools.getClassByPath(clazz.getProject(), folder + "." + class_name);
            if(found != null)
                return found;
        }
        // Testing class_name itself
        return Tools.getClassByPath(clazz.getProject(), class_name);
    }

    public static PsiClass getPsiClass(SimpleXmlParameterEditor editor){
        return Tools.getClassByPath(editor.getProject(), getClassPath(editor));
    }

    public static String getClassPath(SimpleXmlParameterEditor editor){
        return editor.getRootElement().getChildText("class", editor.getRootElement().getNamespace());
    }

    /*
    public static void dispose(SkinEditor editor){
        Iterator<Map.Entry<Component, SkinEditor>> iterator = cached.entrySet().iterator();
        iterator.forEachRemaining(entry -> {
            if(entry.getValue().equals(editor))
                iterator.remove();
        });
    }

     */
}
