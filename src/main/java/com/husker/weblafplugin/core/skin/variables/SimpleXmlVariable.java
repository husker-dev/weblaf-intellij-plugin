package com.husker.weblafplugin.core.skin.variables;

import com.husker.weblafplugin.core.skin.AbstractSkinEditor;
import org.jdom.Element;

public class SimpleXmlVariable extends XmlVariable {

    private String head;

    public SimpleXmlVariable(AbstractSkinEditor editor, String head){
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
        saveElement();
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
