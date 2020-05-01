package com.husker.weblafplugin.variables;

import com.husker.weblafplugin.editors.skin.editor.SkinEditorUI;
import org.jdom.Element;

public class SimpleXmlVariable extends XmlVariable {

    private String head;

    public SimpleXmlVariable(SkinEditorUI editor, String head){
        super(editor);
        this.head = head;
    }

    public void setValue(Object value) {
        if(value.toString().isEmpty()){
            getElement().removeChild(head, getNamespace());
        }else {
            if (getElement().getChild(head, getNamespace()) != null)
                getElement().getChild(head, getNamespace()).setText(value.toString());
            else
                getElement().addContent(new Element(head, getNamespace()) {{
                    setText(value.toString());
                }});
        }
        getEditor().setSkinElement(getElement());
    }

    public Object getValue() {
        return getElement().getChildText(head, getNamespace());
    }

    public String getHeadName(){
        return head;
    }

    public void setHeadName(String head){
        this.head = head;
    }
}
