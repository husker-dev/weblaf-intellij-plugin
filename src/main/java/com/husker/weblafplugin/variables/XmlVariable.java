package com.husker.weblafplugin.variables;

import com.husker.weblafplugin.editors.skin.editor.SkinEditorUI;
import org.jdom.Element;
import org.jdom.Namespace;

public abstract class XmlVariable extends Variable {

    private SkinEditorUI editor;

    public XmlVariable(SkinEditorUI editor){
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

    public SkinEditorUI getSkinEditor(){
        return editor;
    }

    public void saveElement(){
        getSkinEditor().setSkinElement(getElement());
    }
}
