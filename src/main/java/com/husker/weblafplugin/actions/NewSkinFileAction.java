package com.husker.weblafplugin.actions;

import com.alee.api.annotations.NotNull;
import com.husker.weblafplugin.dialogs.SkinCreationDialog;
import com.husker.weblafplugin.tools.Tools;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;

import java.util.Objects;

public class NewSkinFileAction extends AnAction {

    public static final String pattern =
            "<skin xmlns=\"http://weblookandfeel.com/XmlSkin\">\n" +
            "\n" +
            "    <!--Skin settings-->\n" +
            "    <id>[id]</id>\n" +
            "    <class>[class]</class>\n" +
            "    <supportedSystems>all</supportedSystems>\n" +
            "\n" +
            "    <!--Skin information-->\n" +
            "    <title>[title]</title>\n" +
            "    <description>Your description</description>\n" +
            "    <author>[author]</author>\n" +
            "\n" +
            "</skin>";

    public void actionPerformed(@NotNull AnActionEvent event) {
        SkinCreationDialog dialog = new SkinCreationDialog(event);
        dialog.show();
        if(dialog.isOK()){

            String text = pattern;
            text = text.replace("[id]", dialog.getId());
            text = text.replace("[class]", dialog.getXmlStyleClass());
            text = text.replace("[title]", dialog.getTitle());
            text = text.replace("[author]", dialog.getAuthor());

            PsiFile file = PsiFileFactory.getInstance(Objects.requireNonNull(event.getProject())).createFileFromText(dialog.getFileName() + ".xml", XmlFileType.INSTANCE, text);
            Tools.createAndOpen(event, file);
        }
    }

}
