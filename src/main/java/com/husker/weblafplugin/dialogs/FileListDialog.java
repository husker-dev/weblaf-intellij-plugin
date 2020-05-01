package com.husker.weblafplugin.dialogs;

import com.husker.weblafplugin.tools.Tools;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.DoubleClickListener;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class FileListDialog extends DialogWrapper {

    private final JPanel component;
    private final JBList fieldList;

    public FileListDialog(Project project, FileType fileType) {
        this(project, fileType, "");
    }
    public FileListDialog(Project project, FileType fileType, String contains) {
        super(project);

        setTitle("Select File");

        VirtualFile[] files = Tools.getFilesByFileType(project, fileType);

        ArrayList<PsiFile> psies = new ArrayList<>();
        for(int i = 0; i < files.length; i++) {
            if(contains == null)
                break;

            if(files[i].getPath().contains(contains))
                psies.add(Tools.getPsi(project, files[i]));
        }

        CollectionListModel<PsiFile> classes = new CollectionListModel<>(psies);
        fieldList = new JBList(classes);
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        new DoubleClickListener(){
            protected boolean onDoubleClick(MouseEvent event) {
                if (fieldList.getSelectedValuesList().size() > 0) {
                    doOKAction();
                    return true;
                }
                return false;
            }
        }.installOn(fieldList);

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        decorator.disableAddAction();
        decorator.disableRemoveAction();
        decorator.disableUpDownActions();

        component = decorator.createPanel();
        component.setPreferredSize(new Dimension(300, 300));

        init();
    }


    protected JComponent createCenterPanel() {
        return component;
    }

    public PsiFile getPsiFile(){
        show();
        if(isOK() && fieldList.getSelectedValuesList().size() == 1)
            return ((PsiFile)fieldList.getSelectedValue());
        return null;
    }
}
