package com.husker.weblafplugin.skin.core.parameters;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TextButtonParameter extends TextParameter {

    protected JButton button;
    protected String btn_text;

    public TextButtonParameter(String name, String btn_text, int width){
        super(name, width);
        this.btn_text = btn_text;
    }

    public void onInit(){
        super.onInit();

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
