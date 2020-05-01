package com.husker.weblafplugin.parameters;

import com.husker.weblafplugin.components.*;
import com.husker.weblafplugin.components.parameter.Parameter;
import com.husker.weblafplugin.tools.ComponentTools;
import com.husker.weblafplugin.variables.ValueChangedListener;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.StartupUiUtil;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;

// TODO Parameter need to use something like JTextArea instead of JTextField (LazyTextArea), because JTextArea has visual artifacts
public class TextAreaParameter extends Parameter {

    protected LazyTextArea textArea;
    protected JBScrollPane scrollPane;
    protected JPanel panel;

    public TextAreaParameter(String name) {
        super(name);

        add(panel = new JPanel(){{
            setLayout(new BorderLayout());
            add(scrollPane = new JBScrollPane(textArea = new LazyTextArea()));
        }});

        ComponentTools.setWidth(panel, DEFAULT_WIDTH);
        ComponentTools.setHeight(panel, DEFAULT_HEIGHT);

        textArea.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        if(StartupUiUtil.isUnderDarcula()) {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
            scrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(100, 100, 100)));
        }else {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
            scrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, UIUtil.getBoundsColor()));
        }
    }

    public boolean haveErrors() {
        return false;
    }

    public void setText(String text){
        textArea.setText(text);
    }

    public String getText(){
        return textArea.getText();
    }

    public void setAdditionalComponent(Component component){
        int width = (int)component.getPreferredSize().getWidth();

        int new_preferred_width = (int)(textArea.getPreferredSize().getWidth() - width - 5);
        textArea.setPreferredSize(new Dimension(new_preferred_width, (int) textArea.getPreferredSize().getHeight()));

        int new_minimum_width = (int)(textArea.getMinimumSize().getWidth() - width - 5);
        textArea.setMinimumSize(new Dimension(new_minimum_width, (int) textArea.getMinimumSize().getHeight()));

        int new_maximum_width = (int)(textArea.getMaximumSize().getWidth() - width - 5);
        textArea.setMaximumSize(new Dimension(new_maximum_width, (int) textArea.getMaximumSize().getHeight()));

        add(component);
    }

    public void addTextFieldListener(TextChangedListener textChangedListener){
        textArea.addTextChangedListener(textChangedListener);
    }

    public void addValueChangedListener(ValueChangedListener listener) {
        addTextFieldListener(textField -> listener.changed(textField.getText()));
    }

    public void onValueApplying(Object object) {
        textArea.setEventsEnabled(false);
        if(object == null)
            textArea.setText("");
        else
            textArea.setText(object.toString());
        textArea.setEventsEnabled(true);
    }
}
