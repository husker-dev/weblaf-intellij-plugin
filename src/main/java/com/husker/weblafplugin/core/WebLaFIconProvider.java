package com.husker.weblafplugin.core;

import com.intellij.ide.IconProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import icons.MyFileIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.husker.weblafplugin.core.WebLaFTypeChecker.*;

public class WebLaFIconProvider extends IconProvider {

    public Icon getIcon(@NotNull PsiElement element, int flags) {
        try {
            PsiFile containingFile = element.getContainingFile();
            if (containingFile == null || !containingFile.getName().endsWith(".xml"))
                return null;

            //boolean isDark = StartupUiUtil.isUnderDarcula();

            switch (WebLaFTypeChecker.getType(containingFile.getText())) {
                case SKIN:
                    return MyFileIcons.SKIN;
                case STYLE:
                    return MyFileIcons.STYLE;
                case SKIN_AND_STYLE:
                    return MyFileIcons.MIXED;
                case UNKNOWN:
                    return null;
            }
            return null;
        }catch (Exception ex){
            return null;
        }
    }
}
