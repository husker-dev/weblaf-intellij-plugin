package com.husker.weblafplugin.skin.variables.impl;


import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.skin.SkinEditor;
import com.husker.weblafplugin.skin.variables.XmlVariable;
import org.jdom.Element;

import java.util.ArrayList;

public class IconSetsVariable extends XmlVariable {

    public IconSetsVariable(SimpleXmlParameterEditor editor) {
        super(editor);
    }

    public void setValue(Object object) {
        getElement().removeChildren("iconSet", getNamespace());
        if(object != null) {
            for (String element : (String[]) object)
                getElement().addContent(new Element("iconSet", getNamespace()) {{
                    setText(element);
                }});
        }
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
