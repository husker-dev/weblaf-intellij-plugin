package com.husker.weblafplugin.skin.variables;

import com.husker.weblafplugin.skin.SkinEditor;
import org.jdom.Element;
import org.jdom.Namespace;

public abstract class XmlVariable extends Variable {

    private SkinEditor editor;

    public XmlVariable(SkinEditor editor){
        this.editor = editor;
    }

    public Element getElement(){
        return editor.getSkinElement();
    }

    public void setElement(Element element){
        editor.setSkinElement(element);
    }

    public Namespace getNamespace(){
        return getElement().getNamespace();
    }

    public SkinEditor getSkinEditor(){
        return editor;
    }

    public void saveElement(){
        getSkinEditor().setSkinElement(getElement());
    }
}
