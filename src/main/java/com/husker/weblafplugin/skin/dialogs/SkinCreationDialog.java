package com.husker.weblafplugin.skin.dialogs;

import com.alee.managers.style.XmlSkin;
import com.husker.weblafplugin.core.components.textfield.magic.MagicTextField;
import com.husker.weblafplugin.core.components.textfield.magic.impl.MagicClassContent;
import com.husker.weblafplugin.core.dialogs.ClassChooserDialog;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SkinCreationDialog extends DialogWrapper {

    private int text_width = 70;
    private JTextField title;

    private ComboBox<String> class_create_type;
    private MagicTextField class_path;
    private JButton class_chooser_btn;

    private AnActionEvent event;

    public SkinCreationDialog(AnActionEvent event) {
        super(event.getProject());
        this.event = event;

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
                    updateClassPathText();
                    updateOkButton();
                }
            });
        }};

        class_create_type = new ComboBox<>();
        class_create_type.addItem("Create automatically");
        class_create_type.addItem("Choose existing");
        class_create_type.addItem("Do nothing");
        class_create_type.setSelectedIndex(0);
        class_create_type.addActionListener(e -> {
            if(class_create_type.getSelectedIndex() != 0)
                class_path.setText("");
            updateClassPathButtons();
            updateClassPathText();
            updateOkButton();
        });

        class_path = new MagicTextField(new MagicClassContent(event.getProject()));
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
            PsiClass clazz = new ClassChooserDialog(event.getProject(), "Select Skin Class", XmlSkin.class).getPsiClass();
            if (clazz != null)
                class_path.setText(clazz.getQualifiedName());
        });

        updateClassPathButtons();
        updateClassPathText();
        updateOkButton();

        init();
        setTitle("New WebLaF Skin");
    }

    private void updateClassPathButtons(){
        if(class_create_type.getSelectedIndex() == 0){
            class_path.setEnabled(false);
            class_chooser_btn.setEnabled(false);

            MagicClassContent content = (MagicClassContent) class_path.getMagicContent();
            content.setDefaultIcon(AllIcons.Nodes.Class);
        }else{
            MagicClassContent content = (MagicClassContent) class_path.getMagicContent();
            content.setDefaultIcon(null);
        }
        if(class_create_type.getSelectedIndex() == 1){
            class_path.setEnabled(true);
            class_chooser_btn.setEnabled(true);
        }
        if(class_create_type.getSelectedIndex() == 2){
            class_path.setEnabled(false);
            class_chooser_btn.setEnabled(false);
        }
    }

    private void updateClassPathText(){
        if(class_create_type.getSelectedIndex() == 0) {
            PsiDirectory directory = Tools.getSelectedDirectory(event);
            VirtualFile src_root = ProjectFileIndex.getInstance(event.getProject()).getSourceRootForFile(directory.getVirtualFile());

            String class_path = directory.getVirtualFile().getPath().replace(src_root.getPath(), "").substring(1).replace("/", ".");
            String class_name = Tools.formatClassName(title.getText().isEmpty() ? "MySkin" : title.getText().toLowerCase());

            this.class_path.setText(class_path + "." + class_name);
        }
    }

    private void updateOkButton(){
        setOKActionEnabled(!title.getText().isEmpty());
    }

    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 0));

        panel.setLayout(new VerticalFlowLayout());
        panel.add(createParameter("Title", title));
        panel.add(createParameter("Class", class_create_type));
        panel.add(createParameter("Path", class_path, class_chooser_btn));

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
        return title.getText();
    }

    public String getClassPath(){
        return class_path.getText();
    }

    public int getClassCreationType(){
        return class_create_type.getSelectedIndex();
    }

    /*
    public String getAuthor(){
        return System.getProperty("user.name");
    }
    public String getId(){
        return getTitle().toLowerCase().replaceAll("\\s",".");
    }
    public String getFileName(){
        return getTitle().toLowerCase().replaceAll("\\s","-");
    }

     */
}
