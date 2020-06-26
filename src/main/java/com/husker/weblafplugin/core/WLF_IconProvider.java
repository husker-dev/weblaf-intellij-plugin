package com.husker.weblafplugin.core;

import com.intellij.ide.IconProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import icons.MyFileIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.util.List;

import static com.husker.weblafplugin.core.WLF_TypeChecker.*;

public class WLF_IconProvider extends IconProvider {

    public Icon getIcon(@NotNull PsiElement element, int flags) {
        try {
            PsiFile containingFile = element.getContainingFile();
            if (containingFile == null || !containingFile.getName().endsWith(".xml"))
                return null;

            List<Integer> types = WLF_TypeChecker.getTypes(containingFile.getText());

            if(types.contains(SKIN) && types.contains(STYLE))
                return MyFileIcons.MIXED;
            if(types.contains(EXTENSION))
                return MyFileIcons.EXTENSION;
            if(types.contains(SKIN))
                return MyFileIcons.SKIN;
            if(types.contains(STYLE))
                return MyFileIcons.STYLE;

            return null;
        }catch (Exception ex){
            return null;
        }
    }
}
