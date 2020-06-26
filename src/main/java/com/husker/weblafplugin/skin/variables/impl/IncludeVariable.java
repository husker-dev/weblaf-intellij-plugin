package com.husker.weblafplugin.skin.variables.impl;

import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.skin.include.IncludeElement;
import com.husker.weblafplugin.skin.SkinEditor;
import com.husker.weblafplugin.skin.managers.SkinEditorManager;
import com.husker.weblafplugin.skin.variables.XmlVariable;
import com.intellij.openapi.project.Project;
import org.jdom.Element;

import java.util.ArrayList;

public class IncludeVariable extends XmlVariable {

    public IncludeVariable(SimpleXmlParameterEditor editor) {
        super(editor);
    }

    public void setValue(Object object) {
        getElement().removeChildren("include", getNamespace());
        if(object != null) {
            for (IncludeElement element : (IncludeElement[]) object)
                getElement().addContent(element.generateElement(getNamespace()));
        }
        saveElement();
    }

    public Object getValue() {
        ArrayList<IncludeElement> elements = new ArrayList<>();

        SimpleXmlParameterEditor editor = getEditor();
        Project project = editor.getProject();
        String resource_path = SkinEditorManager.Resources.getResourcePath(editor);

        for(Element element : getElement().getChildren())
            if (element.getName().equals("include"))
                elements.add(new IncludeElement(project, resource_path, element.getText(), element.getAttributeValue("nearClass")));

        return elements.toArray(new IncludeElement[0]);
    }
}
