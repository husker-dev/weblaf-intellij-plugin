package com.husker.weblafplugin.extension.components.list.extend;

import com.husker.weblafplugin.core.components.list.FileList;
import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.core.tools.Tools;
import com.husker.weblafplugin.core.tools.XmlTools;
import com.husker.weblafplugin.skin.managers.SkinEditorManager;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import org.jdom.Element;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.Scanner;

public class ExtendsList extends FileList<String> {

    private final SimpleXmlParameterEditor editor;
    private final Project project;

    public ExtendsList(SimpleXmlParameterEditor editor) {
        super();
        this.editor = editor;
        this.project = editor.getProject();
        setCellRenderer(new ExtendsListRenderer());
        setDragEnabled(true);
    }

    public Project getProject(){
        return project;
    }

    protected void updateCachedData(){
        for(VirtualFile file : Tools.getFilesByFileTypeInProject(getProject(), XmlFileType.INSTANCE)){
            if(!"xml".equals(file.getExtension()))
                continue;
            try {
                Scanner scanner = new Scanner(file.getInputStream());

                StringBuilder builder = new StringBuilder();
                while(scanner.hasNext())
                    builder.append(scanner.nextLine()).append("\n");

                Element root = XmlTools.getElement(builder.toString());
                if(root.getNamespace().getURI().equals("http://weblookandfeel.com/XmlSkin")){
                    if( root.getChild("id", root.getNamespace()) == null ||
                            root.getChild("icon", root.getNamespace()) == null ||
                            root.getChild("class", root.getNamespace()) == null ||
                            root.getChild("id", root.getNamespace()).getText() == null ||
                            root.getChild("icon", root.getNamespace()).getText() == null ||
                            root.getChild("class", root.getNamespace()).getText() == null)
                        continue;

                    String id = root.getChild("id", root.getNamespace()).getText();
                    String icon = root.getChild("icon", root.getNamespace()).getText();
                    String clazz = root.getChild("class", root.getNamespace()).getText();

                    for(String element_id : getContent()){
                        if(element_id.equals(id)) {

                            PsiClass psiClass = Tools.getClassByPath(getProject(), clazz);
                            PsiClass resourcePsiClass = SkinEditorManager.Resources.getResourcePsiClass(psiClass);

                            if(resourcePsiClass.getContainingFile() == null)
                                continue;

                            String filePath = resourcePsiClass.getContainingFile().getVirtualFile().getParent().getPath() + "/" + icon;
                            VirtualFile iconFile = Tools.getVirtualFile(filePath);

                            if (iconFile == null || iconFile.getInputStream() == null)
                                continue;

                            cache("icon", element_id, new ImageIcon(ImageIO.read(iconFile.getInputStream())));
                        }
                    }
                }


            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    protected boolean haveError(String element) {
        return false;
    }

    public SimpleXmlParameterEditor getSkinEditor(){
        return editor;
    }
}
