package com.husker.weblafplugin.parameters;

import com.husker.weblafplugin.components.parameter.Parameter;
import com.husker.weblafplugin.variables.ValueChangedListener;

import javax.swing.*;

public class LabelParameter extends Parameter {

    private JLabel label;

    public LabelParameter(String name){
        this(name, "");
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

    public LabelParameter(String name, String text) {
        super(name);
        add(label = new JLabel(text){{
            setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
        }});
    }

    public String getText(){
        return label.getText();
    }
}
