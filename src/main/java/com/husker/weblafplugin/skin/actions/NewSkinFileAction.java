package com.husker.weblafplugin.skin.actions;

import com.alee.api.annotations.NotNull;
import com.husker.weblafplugin.skin.dialogs.SkinCreationDialog;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Objects;

public class NewSkinFileAction extends AnAction {

    /*
        Variables:
            [id]
            [class]
            [title]
            [author]
     */
    public static final String XmlPattern =
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

    /*
        Variables:
            [package]
            [title]
            [author]
            [className]
            [fileName]
     */
    public static final String ClassPattern =
            "[package]\n" +
            "\n" +
            "import com.alee.api.resource.ClassResource;\n" +
            "import com.alee.managers.style.XmlSkin;\n" +
            "\n" +
            "/**\n" +
            " * [title].\n" +
            " *\n" +
            " * @author [author]\n" +
            " */\n" +
            "public class [className] extends XmlSkin {\n" +
            "\n" +
            "    public [className]() {\n" +
            "        super(new ClassResource([className].class, \"[fileName]\"));\n" +
            "    }\n" +
            "}";

    public void actionPerformed(@NotNull AnActionEvent event) {
        SkinCreationDialog dialog = new SkinCreationDialog(event);
        dialog.show();
        if(dialog.isOK()){

            String classPath = "";
            if(dialog.getClassCreationType() == 0){
                String className = Tools.formatClassName(dialog.getTitle().toLowerCase());

                PsiClass psiClass = JavaDirectoryService.getInstance().createClass(Tools.getSelectedDirectory(event), className);
                classPath = psiClass.getQualifiedName();

                // Formatting class
                File classFile = new File(psiClass.getContainingFile().getVirtualFile().getPath());
                try {
                    String fileText = FileUtils.readFileToString(classFile, "utf-8");
                    String packageText = fileText.split(";")[0] + ";";

                    String newText = ClassPattern;
                    newText = newText.replace("[package]", packageText);
                    newText = newText.replace("[title]", dialog.getTitle());
                    newText = newText.replace("[author]", getAuthor(dialog));
                    newText = newText.replace("[className]", getClassName(classPath));
                    newText = newText.replace("[fileName]", formatFileName(dialog) + ".xml");

                    FileUtils.write(classFile, newText, "utf-8");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            if(dialog.getClassCreationType() == 1)
                classPath = dialog.getClassPath();
            if(dialog.getClassCreationType() == 2)
                classPath = "yourClass";

            String text = XmlPattern;
            text = text.replace("[id]", getId(dialog));
            text = text.replace("[class]", classPath);
            text = text.replace("[title]", dialog.getTitle());
            text = text.replace("[author]", getAuthor(dialog));


            PsiFile file = PsiFileFactory.getInstance(Objects.requireNonNull(event.getProject())).createFileFromText(formatFileName(dialog) + ".xml", XmlFileType.INSTANCE, text);
            Tools.createAndOpen(event, file);
        }
    }

    private String getId(SkinCreationDialog dialog){
        return dialog.getTitle().toLowerCase().replaceAll("\\s",".");
    }

    private String getAuthor(SkinCreationDialog dialog){
        return System.getProperty("user.name");
    }

    private String formatFileName(SkinCreationDialog dialog){
        return dialog.getTitle().toLowerCase().replaceAll("\\s","-");
    }

    private String getClassName(String classPath){
        if(!classPath.contains("."))
            return classPath;
        return classPath.substring(classPath.lastIndexOf(".") + 1);
    }
}
