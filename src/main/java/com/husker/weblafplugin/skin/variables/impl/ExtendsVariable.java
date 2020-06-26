package com.husker.weblafplugin.skin.variables.impl;

import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.skin.managers.SkinEditorManager;
import com.husker.weblafplugin.skin.variables.XmlVariable;
import com.intellij.openapi.project.Project;
import org.jdom.Element;

import java.util.ArrayList;

public class ExtendsVariable extends XmlVariable {


    public ExtendsVariable(SimpleXmlParameterEditor editor) {
        super(editor);
    }

    public void setValue(Object object) {
        getElement().removeChildren("extends", getNamespace());
        if(object != null) {
            for (String element : (String[]) object)
                getElement().addContent(element);
        }
        saveElement();
    }

    public Object getValue() {
        ArrayList<String> elements = new ArrayList<>();


        for(Element element : getElement().getChildren())
            if (element.getName().equals("extends"))
                elements.add(element.getText());

        return elements.toArray(new String[0]);
    }
}
