package com.husker.weblafplugin.skin.variables;

import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.skin.SkinEditor;
import org.jdom.Element;
import org.jdom.Namespace;

public abstract class XmlVariable extends Variable {

    private SimpleXmlParameterEditor editor;

    public XmlVariable(SimpleXmlParameterEditor editor){
        this.editor = editor;
    }

    public Element getElement(){
        return editor.getRootElement();
    }

    public void setElement(Element element){
        editor.setRootElement(element);
    }

    public Namespace getNamespace(){
        return getElement().getNamespace();
    }

    public SimpleXmlParameterEditor getEditor(){
        return editor;
    }

    public void saveElement(){
        getEditor().setRootElement(getElement());
    }
}
