package com.husker.weblafplugin.core.components.list;

import com.husker.weblafplugin.core.tools.Tools;
import com.husker.weblafplugin.skin.components.list.include.IncludeList;
import com.husker.weblafplugin.skin.managers.SkinEditorManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.awt.FlowLayout.LEFT;
import static java.awt.FlowLayout.RIGHT;

public abstract class FileCellRenderer<T> extends JPanel implements ListCellRenderer<T> {

    private JPanel left_panel, right_panel;

    private JList<? extends T> list;
    private T element;
    private int index;
    private boolean selected, focused;
    private int indent = 4;

    private ArrayList<JLabel> firstBg = new ArrayList<>();
    private ArrayList<JLabel> secondBg = new ArrayList<>();

    private static final JBColor LibraryFileColor = new JBColor(new Color(255, 255, 228), new Color(79, 75, 65));
    private static final JBColor ErrorColor = new JBColor(new Color(255, 170, 170), new Color(85, 55, 55));

    public static JBColor getLibraryBackgroundColor(){
        return LibraryFileColor;
    }
    public static JBColor getErrorColor(){
        return ErrorColor;
    }

    public FileCellRenderer(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 20));

        add(left_panel = new JPanel(){{
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setOpaque(false);
        }}, BorderLayout.WEST);
        add(right_panel = new JPanel(){{
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setOpaque(false);
        }}, BorderLayout.EAST);

        initComponents();
    }

    public void addToLeft(Component component){
        // separator
        left_panel.add(new JPanel() {{
            setOpaque(false);
            setPreferredSize(new Dimension(indent, 0));
            setBorder(BorderFactory.createEmptyBorder());
        }});
        left_panel.add(component);
    }
    public void addToRight(Component component){
        right_panel.add(new JPanel() {{
            setOpaque(false);
            setPreferredSize(new Dimension(indent, 0));
            setBorder(BorderFactory.createEmptyBorder());
        }}, 0);
        right_panel.add(component, 0);
    }

    public Component getListCellRendererComponent(JList<? extends T> list, T element, int index, boolean selected, boolean focused) {
        this.list = list;
        this.element = element;
        this.index = index;
        this.selected = selected;
        this.focused = focused;

        // Update background
        setBackground(getBackgroundColor());

        // Text color
        for(JLabel label : firstBg)
            label.setForeground(getFirstColor());
        for(JLabel label : secondBg)
            label.setForeground(getSecondColor());

        try {
            updateContent();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    public Color getFirstColor(){
        return UIUtil.getTreeForeground(selected, focused);
    }

    public Color getSecondColor(){
        if(selected || focused || haveError())
            return getFirstColor();
        else
            return UIUtil.getInactiveTextColor();
    }

    private Color getBackgroundColor(){
        if(!selected && !focused && !haveError()){
            if(getFilePath().contains(".jar!"))
                return LibraryFileColor;            // Inside library
            else
                return UIUtil.getListBackground();  // Default
        }

        // Selected / focused
        if(selected)
            return UIUtil.getListSelectionBackground(focused);

        // Error
        if(haveError())
            return ErrorColor;

        return UIUtil.getListBackground();
    }

    public abstract void initComponents();
    public abstract void updateContent();
    public abstract boolean haveError();

    public String getFilePath(){
        return "";
    }

    public JList<? extends T> getList(){
        return list;
    }

    public T getElement(){
        return element;
    }

    public int getIndex(){
        return index;
    }

    public boolean isSelected(){
        return selected;
    }

    public boolean isFocused() {
        return focused;
    }

    public void addLabel(String name){
        addLabel(name, true);
    }

    public void addLabel(String name, boolean main){
        addToLeft(new JLabel(){{
            setName(name);
            setVerticalTextPosition(CENTER);

            if(main)
                firstBg.add(this);
            else
                secondBg.add(this);
        }});
    }

    public void addLabelToRight(String name){
        addLabelToRight(name, true);
    }

    public void addLabelToRight(String name, boolean main){
        addToRight(new JLabel(){{
            setName(name);
            setVerticalTextPosition(CENTER);

            if(main)
                firstBg.add(this);
            else
                secondBg.add(this);
        }});
    }

    public void addIcon(String name){
        addToLeft(new JLabel(){{
            setName(name);
            setVerticalAlignment(CENTER);
            setHorizontalAlignment(CENTER);
            setPreferredSize(new Dimension(16, 16));
        }});
    }

    public void addIconToRight(String name){
        addToRight(new JLabel(){{
            setName(name);
            setVerticalAlignment(CENTER);
            setHorizontalAlignment(CENTER);
            setPreferredSize(new Dimension(16, 16));
        }});
    }

    public void setLabelText(String name, String text){
        for(Component component : left_panel.getComponents())
            if(component instanceof JLabel && component.getName().equals(name))
                ((JLabel) component).setText(text);
        for(Component component : right_panel.getComponents())
            if(component instanceof JLabel && component.getName().equals(name))
                ((JLabel) component).setText(text);
    }

    public void setIcon(String name, Icon icon){
        for(Component component : left_panel.getComponents())
            if(component instanceof JLabel && component.getName().equals(name))
                ((JLabel) component).setIcon(icon);
        for(Component component : right_panel.getComponents())
            if(component instanceof JLabel && component.getName().equals(name))
                ((JLabel) component).setIcon(icon);
    }

    public JLabel getIcon(String name){
        for(Component component : left_panel.getComponents())
            if(component instanceof JLabel && component.getName().equals(name))
                return (JLabel) component;
        for(Component component : right_panel.getComponents())
            if(component instanceof JLabel && component.getName().equals(name))
                return (JLabel) component;
        return null;
    }

    public JLabel getLabel(String name){
        for(Component component : left_panel.getComponents())
            if(component instanceof JLabel && component.getName().equals(name))
                return (JLabel) component;
        for(Component component : right_panel.getComponents())
            if(component instanceof JLabel && component.getName().equals(name))
                return (JLabel) component;
        return null;
    }

}
