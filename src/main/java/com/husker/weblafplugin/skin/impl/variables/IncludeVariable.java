package com.husker.weblafplugin.skin.impl.variables;

import com.husker.weblafplugin.skin.core.IncludeElement;
import com.husker.weblafplugin.skin.core.SkinEditor;
import com.husker.weblafplugin.skin.core.variables.XmlVariable;
import com.intellij.openapi.project.Project;
import org.jdom.Element;

import java.util.ArrayList;

public class IncludeVariable extends XmlVariable {

    public IncludeVariable(SkinEditor editor) {
        super(editor);
    }

    public void setValue(Object object) {
        getElement().removeChildren("include", getNamespace());
        for(IncludeElement element : (IncludeElement[]) object)
            getElement().addContent(element.generateElement(getNamespace()));
        saveElement();
    }

    public Object getValue() {
        ArrayList<IncludeElement> elements = new ArrayList<>();

        SkinEditor skinEditor = getSkinEditor();
        Project project = skinEditor.getProject();
        String resource_path = skinEditor.getResourcePath();

        for(Element element : getElement().getChildren())
            if (element.getName().equals("include"))
                elements.add(new IncludeElement(project, resource_path, element.getText(), element.getAttributeValue("nearClass")));

        return elements.toArray(new IncludeElement[0]);
    }
}
