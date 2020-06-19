package com.husker.weblafplugin.skin.dialogs;

import com.alee.managers.style.XmlSkin;
import com.husker.weblafplugin.core.components.textfield.magic.MagicTextField;
import com.husker.weblafplugin.core.components.textfield.magic.impl.MagicClassContent;
import com.husker.weblafplugin.core.dialogs.ClassChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.psi.PsiClass;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SkinCreationDialog extends DialogWrapper {

    private int text_width = 80;
    private JTextField title;

    private JCheckBox class_create;
    private MagicTextField class_path;
    private JButton class_chooser_btn;

    public SkinCreationDialog(Project project) {
        super(project);

        title = new JTextField(){{
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
                    updateOkButton();
                }
            });
        }};

        class_create = new JCheckBox("Create new class automatically");
        class_create.setSelected(true);
        class_create.addActionListener(e -> {
            updateOkButton();
            class_path.setEnabled(!class_create.isSelected());
            class_chooser_btn.setEnabled(!class_create.isSelected());
        });

        class_path = new MagicTextField(new MagicClassContent(project));
        class_path.setEnabled(!class_create.isSelected());
        class_path.getDocument().addDocumentListener(new DocumentListener() {
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
                updateOkButton();
            }
        });

        class_chooser_btn = new JButton("...");
        class_chooser_btn.setPreferredSize(new Dimension(30, 22));
        class_chooser_btn.addActionListener(e -> {
            PsiClass clazz = new ClassChooserDialog(project, "Select Skin Class", XmlSkin.class).getPsiClass();

            if (clazz != null)
                class_path.setText(clazz.getQualifiedName());
        });
        class_chooser_btn.setEnabled(!class_create.isSelected());

        updateOkButton();

        init();
        setTitle("New WebLaF Skin");
    }

    private void updateOkButton(){
        if(title.getText().isEmpty()){
            setOKActionEnabled(false);
            return;
        }
        if(!class_create.isSelected() && class_path.getText().isEmpty()){
            setOKActionEnabled(false);
            return;
        }
        setOKActionEnabled(true);
    }

    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 0));

        panel.setLayout(new VerticalFlowLayout());
        panel.add(createParameter("Title", title));
        panel.add(createParameter(class_create));
        panel.add(createParameter("Class", class_path, class_chooser_btn));

        return panel;
    }

    private JPanel createParameter(JComponent component){
        return createParameter("", component);
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
        return new JLabel(text + (text.isEmpty() ? "" : ":")){{
            setPreferredSize(new Dimension(text_width, 22));
        }};
    }

    public String getTitle(){
        if(title.getText().replaceAll("\\s",".").equals(""))
            return "Example";
        return title.getText();
    }
    public String getXmlStyleClass(){
        if(class_path.getText().replaceAll("\\s",".").equals(""))
            return "com.example.XmlSkin";
        return class_path.getText();
    }
    public boolean isAutoCreateClassFile(){
        return class_create.isSelected();
    }
    public String getAuthor(){
        return System.getProperty("user.name");
    }
    public String getId(){
        return getTitle().toLowerCase().replaceAll("\\s",".");
    }
    public String getFileName(){
        return getTitle().toLowerCase().replaceAll("\\s","-");
    }
}
