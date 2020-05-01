package com.husker.weblafplugin.parameters;

import com.husker.weblafplugin.dialogs.SkinClassChooserDialog;
import com.husker.weblafplugin.tools.Tools;
import com.intellij.psi.PsiClass;

public class ClassChooserParameter extends TextButtonParameter {

    public ClassChooserParameter(String name, Class clazz, int width) {
        super(name, "...", width);
        addButtonListener(e -> {
            PsiClass psiClass = new SkinClassChooserDialog(getSkinEditor().getProject(), clazz).getPsiClass();
            if(psiClass != null)
                setText(psiClass.getQualifiedName());
        });
    }
    public ClassChooserParameter(String name, Class clazz) {
        this(name, clazz, DEFAULT_WIDTH);
    }

    public PsiClass getPsiClass(){
        return Tools.getClassByPath(getSkinEditor().getProject(), getText());
    }

    public void addClassChooserListener(ClassChooserListener listener){
        addTextFieldListener(e -> listener.event(getText()));
    }

    public boolean haveErrors() {
        return getPsiClass() == null;
    }

    public interface ClassChooserListener {
        void event(String class_path);
    }
}
