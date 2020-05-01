package com.husker.weblafplugin.parameters;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TextButtonParameter extends TextParameter {

    protected JButton button;

    public TextButtonParameter(String name, String btn_text, int width){
        super(name, width);
        setAdditionalComponent(button = new JButton(){{
            setPreferredSize(new Dimension(50, 25));
        }});
        button.setText(btn_text);
    }

    public TextButtonParameter(String name, String btn_text){
        this(name, btn_text, DEFAULT_WIDTH);
    }

    public TextButtonParameter(String name, int width) {
        this(name, "Btn", width);
    }

    public TextButtonParameter(String name) {
        this(name, "Btn");
    }

    public void setButtonText(String text){
        button.setText(text);
    }

    public void addButtonListener(ActionListener actionListener){
        button.addActionListener(actionListener);
    }
}
