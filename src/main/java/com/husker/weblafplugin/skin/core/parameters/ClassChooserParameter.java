package com.husker.weblafplugin.skin.core.parameters;

import com.husker.weblafplugin.core.components.textfield.magic.MagicContent;
import com.husker.weblafplugin.core.components.textfield.magic.classes.ClassMagicContent;
import com.husker.weblafplugin.core.dialogs.ClassChooserDialog;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.psi.PsiClass;

import javax.swing.*;
import java.util.ArrayList;

public class ClassChooserParameter extends TextButtonParameter {

    private Class<?> clazz;
    private final ArrayList<String> blackList = new ArrayList<>();

    public ClassChooserParameter(String name, Class<?> clazz, int width) {
        super(name, "...", width);
        this.clazz = clazz;
    }
    public ClassChooserParameter(String name, Class<?> clazz) {
        this(name, clazz, DEFAULT_WIDTH);
    }

    public void onInit(){
        super.onInit();

        textField.setMagicPanel(new ClassMagicContent(getSkinEditor().getProject()));

        addButtonListener(e -> {
            ClassChooserDialog dialog = new ClassChooserDialog(getSkinEditor().getProject(), "Select Skin Class", clazz);
            for(String class_path : blackList)
                dialog.addBlackListClass(class_path);

            PsiClass psiClass = dialog.getPsiClass();
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

    public void addBlackListClass(Class<?> clazz){
        addBlackListClass(clazz.getCanonicalName());
    }
    public void addBlackListClass(String class_path){
        blackList.add(class_path);
    }

    public boolean haveErrors() {
        return getPsiClass() == null;
    }

    public interface ClassChooserListener {
        void event(String class_path);
    }
}
