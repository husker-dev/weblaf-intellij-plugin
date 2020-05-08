package com.husker.weblafplugin;

import com.husker.weblafplugin.tools.WebLaFTypeChecker;
import com.intellij.ide.IconProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ui.StartupUiUtil;
import icons.MyFileIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.husker.weblafplugin.tools.WebLaFTypeChecker.*;

public class WebLaFIconProvider extends IconProvider {

    public Icon getIcon(@NotNull PsiElement element, int flags) {
        try {
            PsiFile containingFile = element.getContainingFile();
            if (containingFile == null || !containingFile.getName().endsWith(".xml"))
                return null;

            boolean isDark = StartupUiUtil.isUnderDarcula();

            switch (WebLaFTypeChecker.getType(containingFile.getText())) {
                case SKIN:
                    return isDark ? MyFileIcons.DARK_SKIN : MyFileIcons.LIGHT_SKIN;
                case STYLE:
                    return isDark ? MyFileIcons.DARK_STYLE : MyFileIcons.LIGHT_STYLE;
                case SKIN_AND_STYLE:
                    return isDark ? MyFileIcons.DARK_SKIN_AND_STYLE : MyFileIcons.LIGHT_SKIN_AND_STYLE;
                case UNKNOWN:
                    return null;
            }
            return null;
        }catch (Exception ex){
            return null;
        }
    }
}
