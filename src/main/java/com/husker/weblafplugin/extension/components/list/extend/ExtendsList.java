package com.husker.weblafplugin.extension.components.list.extend;

import com.husker.weblafplugin.core.components.list.FileList;
import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.core.tools.ImageUtils;
import com.husker.weblafplugin.core.tools.Tools;
import com.husker.weblafplugin.core.tools.XmlTools;
import com.husker.weblafplugin.skin.managers.SkinEditorManager;
import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.ui.DoubleClickListener;
import org.jdom.Element;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.Scanner;

public class ExtendsList extends FileList<String> {

    private final SimpleXmlParameterEditor editor;

    public ExtendsList(SimpleXmlParameterEditor editor) {
        super(editor.getProject());
        this.editor = editor;
        setCellRenderer(new ExtendsListRenderer());
        setDragEnabled(true);
        setAutoClearCacheEnabled(false);

        new DoubleClickListener(){
            protected boolean onDoubleClick(MouseEvent event) {
                Tools.openFile(getProject(), Tools.getVirtualFile((String) getCached("path", getSelectedValue())));
                return true;
            }
        }.installOn(this);
    }

    protected void updateCachedData(){
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            ApplicationManager.getApplication().runReadAction(() -> {
                for(VirtualFile file : Tools.getFilesByFileType(getProject(), XmlFileType.INSTANCE)){
                    if(!"xml".equals(file.getExtension()))
                        continue;

                    StringBuilder builder = new StringBuilder();
                    try {
                        Scanner scanner = new Scanner(file.getInputStream());

                        while(scanner.hasNext())
                            builder.append(scanner.nextLine()).append("\n");

                        if(builder.toString().contains("<!DOCTYPE RSyntaxTheme SYSTEM \"theme.dtd\">"))
                            continue;

                        Element root = XmlTools.getElement(builder.toString());
                        if(root.getNamespace().getURI().equals("http://weblookandfeel.com/XmlSkin")){
                            if( root.getChild("id", root.getNamespace()) == null ||
                                    root.getChild("class", root.getNamespace()) == null ||
                                    root.getChild("title", root.getNamespace()) == null ||
                                    root.getChild("id", root.getNamespace()).getText() == null ||
                                    root.getChild("class", root.getNamespace()).getText() == null ||
                                    root.getChild("title", root.getNamespace()).getText() == null)
                                continue;

                            String id = root.getChild("id", root.getNamespace()).getText();
                            String clazz = root.getChild("class", root.getNamespace()).getText();
                            String title = root.getChild("title", root.getNamespace()).getText();
                            String icon;
                            if(root.getChild("icon", root.getNamespace()) != null)
                                icon = root.getChild("icon", root.getNamespace()).getText();
                            else
                                icon = null;


                            String author;
                            if( root.getChild("author", root.getNamespace()) == null ||
                                root.getChild("author", root.getNamespace()).getText() == null)
                                author = null;
                            else
                                author = root.getChild("author", root.getNamespace()).getText();

                            for(String element_id : getContent()){
                                if(element_id.equals(id)) {

                                    PsiClass psiClass = Tools.getClassByPath(getProject(), clazz);
                                    PsiClass resourcePsiClass = SkinEditorManager.Resources.getResourcePsiClass(psiClass);

                                    if(resourcePsiClass == null || resourcePsiClass.getContainingFile() == null)
                                        continue;

                                    if(icon != null) {
                                        String filePath = resourcePsiClass.getContainingFile().getVirtualFile().getParent().getPath() + "/" + icon;
                                        VirtualFile iconFile = Tools.getVirtualFile(filePath);

                                        if (iconFile == null || iconFile.getInputStream() == null)
                                            continue;
                                        cache("icon", element_id, new ImageIcon(ImageUtils.scale(ImageIO.read(iconFile.getInputStream()), 16, 16)));
                                    }else
                                        cache("icon", element_id, null);

                                    cache("title", element_id, title);
                                    cache("author", element_id, author);
                                    cache("exist", element_id, true);
                                    cache("path", element_id, file.getPath());
                                }
                            }
                        }


                    }catch (Exception ex){
                        ex.printStackTrace();
                        System.out.println(builder.toString());
                    }
                }

                for(String element_id : getContent()) {
                    if (getCached("exist", element_id) == null)
                        cache("exist", element_id, false);
                }

                updateUI();
            });
        });


    }

    protected boolean haveError(String element) {
        return false;
    }

    public SimpleXmlParameterEditor getSkinEditor(){
        return editor;
    }
}
