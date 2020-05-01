package com.husker.weblafplugin.editors.skin.editor.variables;

import com.husker.weblafplugin.editors.skin.editor.SkinEditorUI;
import com.husker.weblafplugin.variables.Variable;
import com.husker.weblafplugin.variables.XmlVariable;
import org.jdom.Element;

import java.util.ArrayList;

public class Include extends XmlVariable {

    public Include(SkinEditorUI editor) {
        super(editor);
    }

    public void setValue(Object object) {

    }

    public Object getValue() {
        ArrayList<String> out = new ArrayList<>();
        for(Element element : getElement().getChildren())
            if(element.getName().equals("include"))
                out.add(element.getText());

        return out.toArray(new String[0]);
    }
}
