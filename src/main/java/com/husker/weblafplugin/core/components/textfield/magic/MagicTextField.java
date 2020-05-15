package com.husker.weblafplugin.core.components.textfield.magic;

import com.intellij.ui.components.JBTextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

// Magic name -_-
public class MagicTextField extends JBTextField {

    private JPanel container;
    private MagicContent content;
    private boolean listeners_loaded = false;
    private boolean focused = false;

    public MagicTextField() {
        loadListener();
    }

    public MagicTextField(MagicContent content) {
        setMagicPanel(content);
    }

    public MagicTextField(int columns, MagicContent content) {
        super(columns);
        setMagicPanel(content);
    }

    public MagicTextField(int columns) {
        super(columns);
    }

    public MagicTextField(String text, MagicContent content) {
        super(text);
        setMagicPanel(content);
    }

    public MagicTextField(String text) {
        super(text);
    }

    public MagicTextField(String text, int columns, MagicContent content) {
        super(text, columns);
        setMagicPanel(content);
    }

    public MagicTextField(String text, int columns) {
        super(text, columns);
    }

    public void setMagicPanel(MagicContent content){
        this.content = content;
        content.setBackground(content.getBackgroundColor());

        removeAll();
        setLayout(new BorderLayout());
        add(container = new JPanel(){{
            setOpaque(true);
            setBackground(new Color(0, 0, 0, 0));
            configurePanelPadding(this);
            setLayout(new BorderLayout());
            setVisible(!MagicTextField.this.isFocusOwner());
            content.update(getText());
            add(content);
        }});
        loadListener();
    }

    public void loadListener(){
        if(listeners_loaded)
            return;
        else
            listeners_loaded = true;

        addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent focusEvent) {
                focused = true;
                if(container != null)
                    container.setVisible(false);
            }
            public void focusLost(FocusEvent focusEvent) {
                focused = false;
                if(container != null) {
                    container.setVisible(true);
                    content.update(getText());
                }
            }
        });

        getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                event();
            }
            public void removeUpdate(DocumentEvent e) {
                event();
            }
            public void insertUpdate(DocumentEvent e) {
                event();
            }
            public void event() {
                if(!focused && content != null)
                    content.update(getText());
            }
        });
    }

    public void configurePanelPadding(JPanel panel){
        panel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }

    public boolean isFocused(){
        return focused;
    }
}
