package com.husker.weblafplugin.core;

import com.husker.weblafplugin.core.tools.XmlTools;
import org.jdom.Element;

import java.util.Arrays;
import java.util.List;

public class WebLaFTypeChecker {

    public static final int UNKNOWN = -1;
    public static final int SKIN = 0;
    public static final int STYLE = 1;
    public static final int SKIN_AND_STYLE = 2;

    public static int getType(String text) {
        try {
            Element root = XmlTools.getElement(text);

            if(root == null)
                return UNKNOWN;
            if(!root.getName().equals("skin"))
                return UNKNOWN;
            if(!"http://weblookandfeel.com/XmlSkin".equals(root.getNamespace().getURI()))
                return UNKNOWN;

            boolean styles = false;
            boolean skin = false;

            for (Element element : root.getChildren()) {
                if (element.getName().equals("style")) {
                    styles = true;
                    break;
                }
            }

            List<String> skin_attributes = Arrays.asList("id", "class", "supportedSystems", "icon", "title", "description", "author", "iconSet", "include");
            for (Element element : root.getChildren()) {
                if (skin_attributes.contains(element.getName())) {
                    skin = true;
                    break;
                }
            }

            if (styles) {
                if (skin)
                    return SKIN_AND_STYLE;
                else
                    return STYLE;
            } else
                return SKIN;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return UNKNOWN;
    }
}
