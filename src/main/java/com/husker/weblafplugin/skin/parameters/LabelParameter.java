package com.husker.weblafplugin.skin.parameters;

import com.husker.weblafplugin.skin.components.parameter.Parameter;
import com.husker.weblafplugin.skin.variables.ValueChangedListener;

import javax.swing.*;

public class LabelParameter extends Parameter {

    private JLabel label;
    private String text;

    public LabelParameter(String name){
        this(name, "");
    }

    public LabelParameter(String name, String text) {
        super(name);
        this.text = text;
    }

    public void onInit(){
        add(label = new JLabel(text){{
            setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
        }});
    }

    public void addValueChangedListener(ValueChangedListener listener) {
    }

    public void onValueApplying(Object value) {
        if(value == null)
            label.setText("");
        else
            label.setText(value.toString());
    }

    public boolean haveErrors() {
        return false;
    }

    public void onApply() { }



    public String getText(){
        return label.getText();
    }
}
