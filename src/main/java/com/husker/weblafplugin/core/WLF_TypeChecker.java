package com.husker.weblafplugin.core;

import com.husker.weblafplugin.core.tools.Tools;
import com.husker.weblafplugin.core.tools.XmlTools;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;

import java.util.*;

public class WLF_TypeChecker {

    public static final int UNKNOWN = 0;
    public static final int SKIN = 1;
    public static final int STYLE = 2;
    public static final int EXTENSION = 3;

    public static List<Integer> getTypes(String text) {
        try {
            ArrayList<Integer> types = new ArrayList<>();

            Element root = XmlTools.getElement(text);
            String namespace = (root == null || root.getNamespace().getURI() == null) ? "" : root.getNamespace().getURI();

            if(root == null) {
                types.add(UNKNOWN);
                return types;
            }

            if(namespace.equals("http://weblookandfeel.com/XmlSkinExtension"))
                types.add(EXTENSION);

            if(namespace.equals("http://weblookandfeel.com/XmlSkin")){
                List<String> skin_attributes = Arrays.asList("id", "class", "supportedSystems", "icon", "title", "description", "author");

                for (Element element : root.getChildren()) {
                    if (skin_attributes.contains(element.getName())) {
                        types.add(SKIN);
                        break;
                    }
                }
            }

            if(root.getChild("style", root.getNamespace()) != null)
                types.add(STYLE);

            return types;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.singletonList(UNKNOWN);
    }

    private static String readFile(Project project, VirtualFile file){
        return Tools.getPsi(project, file).getText();
    }

    public static boolean isSkin(String text){
        return getTypes(text).contains(SKIN);
    }
    public static boolean isSkin(Project project, VirtualFile file){
        return isSkin(readFile(project, file));
    }

    public static boolean isStyle(String text){
        return getTypes(text).contains(STYLE);
    }
    public static boolean isStyle(Project project, VirtualFile file){
        return isStyle(readFile(project, file));
    }

    public static boolean isExtension(String text){
        return getTypes(text).contains(EXTENSION);
    }
    public static boolean isExtension(Project project, VirtualFile file){
        return isExtension(readFile(project, file));
    }
}
