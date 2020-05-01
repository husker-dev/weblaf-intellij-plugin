package com.husker.weblafplugin.parameters;

import com.husker.weblafplugin.components.parameter.Parameter;
import com.husker.weblafplugin.components.TextChangedListener;
import com.husker.weblafplugin.components.LazyTextField;
import com.husker.weblafplugin.variables.ValueChangedListener;
import com.husker.weblafplugin.tools.ComponentTools;

import java.awt.*;

public class TextParameter extends Parameter {

    protected LazyTextField textField;

    public TextParameter(String name, int width){
        super(name);
        add(textField = new LazyTextField());
        ComponentTools.setWidth(textField, width);
    }
    public TextParameter(String name) {
       this(name, DEFAULT_WIDTH);
    }

    public boolean haveErrors() {
        return false;
    }

    public void setText(String text){
        textField.setText(text);
    }

    public String getText(){
        return textField.getText();
    }

    public void setAdditionalComponent(Component component){
        int width = (int)component.getPreferredSize().getWidth();

        int new_preferred_width = (int)(textField.getPreferredSize().getWidth() - width - 5);
        textField.setPreferredSize(new Dimension(new_preferred_width, (int)textField.getPreferredSize().getHeight()));

        int new_minimum_width = (int)(textField.getMinimumSize().getWidth() - width - 5);
        textField.setMinimumSize(new Dimension(new_minimum_width, (int)textField.getMinimumSize().getHeight()));

        int new_maximum_width = (int)(textField.getMaximumSize().getWidth() - width - 5);
        textField.setMaximumSize(new Dimension(new_maximum_width, (int)textField.getMaximumSize().getHeight()));

        add(component);
    }

    public void addTextFieldListener(TextChangedListener textChangedListener){
        textField.addTextChangedListener(textChangedListener);
    }

    public void addValueChangedListener(ValueChangedListener listener) {
        addTextFieldListener(textField -> listener.changed(textField.getText()));
    }

    public void onValueApplying(Object object) {
        textField.setEventsEnabled(false);
        if(object == null)
            textField.setText("");
        else
            textField.setText(object.toString());
        textField.setEventsEnabled(true);
    }

}
