package com.husker.weblafplugin.dialogs;

import com.alee.managers.style.XmlSkin;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.psi.PsiClass;

import javax.swing.*;
import java.awt.*;

public class SkinCreationDialog extends DialogWrapper {

    private AnActionEvent event;
    private Project project;

    private int text_width = 80;
    private JTextField title = new JTextField();
    private JTextField tf_class = new JTextField();
    private JButton btn_classes = new JButton("..."){{
        setPreferredSize(new Dimension(30, 22));
        addActionListener(e -> {
            PsiClass clazz = new SkinClassChooserDialog(project, XmlSkin.class).getPsiClass();

            if(clazz != null)
                tf_class.setText(clazz.getQualifiedName());
        });
    }};
    private JTextField author = new JTextField(System.getProperty("user.name"));


    public SkinCreationDialog(AnActionEvent event) {
        super(event.getProject());
        init();
        setTitle("New WebLaF Skin");

        this.event = event;
        this.project = event.getProject();
    }

    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 0));

        panel.setLayout(new VerticalFlowLayout());
        panel.add(createParameter("Title", title));
        panel.add(createParameter("XmlSkin Class", tf_class, btn_classes));
        panel.add(createParameter("Author", author));

        return panel;
    }

    private JPanel createParameter(String name, JComponent component){
        return new JPanel(){{
            setLayout(new BorderLayout());
            add(createLabel(name), BorderLayout.WEST);
            add(component);
        }};
    }
    private JPanel createParameter(String name, JComponent component, JComponent right_component){
        return new JPanel(){{
            setLayout(new BorderLayout());
            add(createLabel(name), BorderLayout.WEST);
            add(component);
            add(right_component, BorderLayout.EAST);
        }};
    }

    private JLabel createLabel(String text){
        return new JLabel(text + ":"){{
            setPreferredSize(new Dimension(text_width, 22));
        }};
    }

    public String getTitle(){
        if(title.getText().replaceAll("\\s",".").equals(""))
            return "Example";
        return title.getText();
    }
    public String getXmlStyleClass(){
        if(tf_class.getText().replaceAll("\\s",".").equals(""))
            return "com.example.XmlSkin";
        return tf_class.getText();
    }
    public String getAuthor(){
        if(author.getText().replaceAll("\\s",".").equals(""))
            return "YourName";
        return author.getText();
    }
    public String getId(){
        return getTitle().toLowerCase().replaceAll("\\s",".");
    }
    public String getFileName(){
        return getTitle().toLowerCase().replaceAll("\\s","-");
    }
}
