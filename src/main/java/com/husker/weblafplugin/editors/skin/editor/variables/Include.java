package com.husker.weblafplugin.editors.skin.editor.variables;

import com.husker.weblafplugin.components.list.include.IncludeElement;
import com.husker.weblafplugin.editors.skin.editor.SkinEditorUI;
import com.husker.weblafplugin.variables.XmlVariable;
import com.intellij.openapi.project.Project;
import org.jdom.Element;

import java.util.ArrayList;

public class Include extends XmlVariable {

    public Include(SkinEditorUI editor) {
        super(editor);
    }

    public void setValue(Object object) {
        getElement().removeChildren("include", getNamespace());
        for(IncludeElement element : (IncludeElement[]) object)
            getElement().addContent(element.getElement(getNamespace()));
        saveElement();
    }

    public Object getValue() {
        ArrayList<IncludeElement> elements = new ArrayList<>();

        SkinEditorUI skinEditor = getSkinEditor();
        Project project = skinEditor.getProject();
        String resource_path = skinEditor.getResourcePath();

        for(Element element : getElement().getChildren())
            if (element.getName().equals("include"))
                elements.add(new IncludeElement(project, resource_path, element.getText(), element.getAttributeValue("nearClass")));

        return elements.toArray(new IncludeElement[0]);
    }
}
