package com.husker.weblafplugin.skin.core.dialogs;

import com.husker.weblafplugin.core.components.textfield.magic.MagicTextField;
import com.husker.weblafplugin.core.components.textfield.magic.classes.MagicClassContent;
import com.husker.weblafplugin.core.components.textfield.magic.path.MagicPathContent;
import com.husker.weblafplugin.core.dialogs.ClassChooserDialog;
import com.husker.weblafplugin.core.dialogs.FileListDialog;
import com.husker.weblafplugin.core.tools.ComponentTools;
import com.husker.weblafplugin.core.tools.Tools;
import com.husker.weblafplugin.skin.core.IncludeElement;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class IncludeElementEditorDialog extends DialogWrapper {

    private final Project project;
    private final String skinClass;
    private String[] blackList;
    private String nearClass;
    private String resourcePath;

    private MagicTextField classPathField;
    private MagicTextField resourcePathField;

    public IncludeElementEditorDialog(String skinClass, IncludeElement element) {
        super(element.getProject());

        setTitle("Include Element");

        this.project = element.getProject();
        if(element.getNearClass() != null && !element.getNearClass().isEmpty())
            this.skinClass = element.getNearClass();
        else
            this.skinClass = skinClass;
        this.nearClass = this.skinClass;
        this.resourcePath = element.getLocalPath();
        init();
    }

    public IncludeElementEditorDialog(Project project, String skinClass) {
        super(project);

        setTitle("Include Element");

        this.project = project;
        this.skinClass = skinClass;
        this.nearClass = this.skinClass;
        init();
    }

    public void setBlackList(String... blackList){
        this.blackList = blackList;
    }

    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(450, 0));
        panel.setLayout(new VerticalFlowLayout());

        classPathField = new MagicTextField(new MagicClassContent(project)){{
            if(skinClass != null)
                setText(skinClass);
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
                    nearClass = getText();
                }
            });
        }};
        JButton classPathBtn = new JButton("..."){{
            addActionListener(e -> {
                PsiClass clazz = new ClassChooserDialog(project, "Select near class", "com.alee.managers.style.XmlSkin").getPsiClass();
                if(clazz != null)
                    classPathField.setText(clazz.getQualifiedName());
            });
        }};

        resourcePathField = new MagicTextField(new MagicPathContent(project)){{
            if(resourcePath != null)
                setText(resourcePath);
        }};
        JButton resourceBtn = new JButton("..."){{
            addActionListener(e -> {
                try {
                    String nearClassPath = Tools.getClassByPath(project, nearClass).getContainingFile().getVirtualFile().getPath();
                    nearClassPath = nearClassPath.substring(0, nearClassPath.lastIndexOf("/")) + "/";

                    PsiFile file = new FileListDialog(project, XmlFileType.INSTANCE, nearClassPath, blackList).getPsiFile();
                    if (file != null) {
                        String path = file.getVirtualFile().getPath().replace(nearClassPath, "");
                        resourcePathField.setText(path);
                    }
                }catch (Exception ex){}
            });
        }};

        panel.add(createTextAndButton("Path", resourcePathField, resourceBtn));
        panel.add(createTextAndButton("Near class", classPathField, classPathBtn));

        return panel;
    }

    public JComponent createTextAndButton(String text, JTextField field, JButton button){
        return new JPanel(){{
            setLayout(new BorderLayout());

            ComponentTools.setWidth(button, 40);
            add(new JLabel(text + ":"){{
                ComponentTools.setWidth(this, 70);
            }}, BorderLayout.WEST);
            add(field);
            add(button, BorderLayout.EAST);
        }};
    }

    public IncludeElement getIncludeElement(){
        show();
        if(isOK()){
            PsiClass clazz = Tools.getClassByPath(project, classPathField.getText());
            if(clazz != null && !resourcePathField.getText().isEmpty()){
                String classFilePath = clazz.getContainingFile().getVirtualFile().getPath();
                String resourcePath = classFilePath.substring(0, classFilePath.lastIndexOf("/")) + "/";

                VirtualFile file = Tools.getVirtualFile(resourcePath + this.resourcePathField.getText());
                if(file != null)
                    return new IncludeElement(project, resourcePath, this.resourcePathField.getText(), nearClass.equals(skinClass) ? null : clazz.getQualifiedName());
            }
        }
        return null;
    }
}
