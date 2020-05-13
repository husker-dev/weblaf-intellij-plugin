package com.husker.weblafplugin.core.skin.variables;

import com.husker.weblafplugin.core.skin.AbstractSkinEditor;
import org.jdom.Element;
import org.jdom.Namespace;

public abstract class XmlVariable extends Variable {

    private AbstractSkinEditor editor;

    public XmlVariable(AbstractSkinEditor editor){
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

    public AbstractSkinEditor getSkinEditor(){
        return editor;
    }

    public void saveElement(){
        getSkinEditor().setSkinElement(getElement());
    }
}
