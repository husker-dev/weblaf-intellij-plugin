package com.husker.weblafplugin.components.list.text;

import com.husker.weblafplugin.components.list.ListElement;

import javax.swing.*;

public class TextListElement<T> extends ListElement<T> {

    public TextListElement(T text){
        super(text);
        setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 0));
        add(new JLabel(text.toString()));
    }
}
