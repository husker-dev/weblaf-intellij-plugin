package com.husker.weblafplugin.skin.parameters;

import com.husker.weblafplugin.core.dialogs.ClassChooserDialog;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.psi.PsiClass;

public class ClassChooserParameter extends TextButtonParameter {

    private Class<?> clazz;

    public ClassChooserParameter(String name, Class<?> clazz, int width) {
        super(name, "...", width);
        this.clazz = clazz;
    }
    public ClassChooserParameter(String name, Class<?> clazz) {
        this(name, clazz, DEFAULT_WIDTH);
    }

    public void onInit(){
        super.onInit();

        addButtonListener(e -> {
            PsiClass psiClass = new ClassChooserDialog(getSkinEditor().getProject(), "Select Skin Class", clazz).getPsiClass();
            if(psiClass != null)
                setText(psiClass.getQualifiedName());
        });
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
