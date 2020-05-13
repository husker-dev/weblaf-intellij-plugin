package com.husker.weblafplugin.skin.variables;


import com.husker.weblafplugin.core.skin.AbstractSkinEditor;
import com.husker.weblafplugin.core.skin.variables.XmlVariable;
import org.jdom.Element;

import java.util.ArrayList;

public class IconSetsVariable extends XmlVariable {

    public IconSetsVariable(AbstractSkinEditor editor) {
        super(editor);
    }

    public void setValue(Object object) {
        getElement().removeChildren("iconSet", getNamespace());
        for(String element : (String[]) object)
            getElement().addContent(new Element("iconSet", getNamespace()){{
                setText(element);
            }});
        saveElement();
    }

    public Object getValue() {
        ArrayList<String> elements = new ArrayList<>();

        for(Element element : getElement().getChildren())
            if (element.getName().equals("iconSet"))
                elements.add(element.getText());

        return elements.toArray(new String[0]);
    }
}
