package com.husker.weblafplugin.skin.components.parameter;

import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.SeparatorFactory;
import com.intellij.ui.TitledSeparator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ParameterBlock extends JPanel {

    protected TitledSeparator title;
    protected JPanel content;
    protected JPanel main_content;

    public ParameterBlock(){
        this("");
    }

    public ParameterBlock(String title){
        this.title = SeparatorFactory.createSeparator(title, null);
        setLayout(new VerticalFlowLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        super.add(this.title);
        super.add(main_content = new JPanel(){{
            setLayout(new BorderLayout());
            add(content = new JPanel(){{
                setLayout(new VerticalFlowLayout(0, 5));
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            }}, BorderLayout.WEST);
        }});
    }

    public void setTitle(String title){
        this.title.setText(title);
    }

    public JPanel getContentPanel(){
        return content;
    }

    public Component add(Component comp) {
        return content.add(comp);
    }

    public Component add(String name, Component comp) {
        return content.add(name, comp);
    }

    public Component add(Component comp, int index) {
        return content.add(comp, index);
    }

    public void add(@NotNull Component comp, Object constraints) {
        content.add(comp, constraints);
    }

    public void add(Component comp, Object constraints, int index) {
        content.add(comp, constraints, index);
    }

    public void add(PopupMenu popup) {
        content.add(popup);
    }

    public void addToRight(Component component){
        main_content.add(component);
    }
}
