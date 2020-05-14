package com.husker.weblafplugin.core.dialogs;

import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.lang.jvm.JvmClassKind;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.StandardProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LoadingDecorator;
import com.intellij.psi.PsiClass;
import com.intellij.psi.util.PsiUtil;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTabbedPane;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ClassChooserDialog extends DialogWrapper {

    private final JPanel component;
    private ListPage page_project, page_all;
    private final ArrayList<String> blackList = new ArrayList<>();
    private Project project;
    private String clazz;
    private JBTabbedPane tabbedPane;

    public ClassChooserDialog(Project project, String title, Class<?> clazz) {
        this(project, title, clazz.getName());
    }

    public ClassChooserDialog(Project project, String title, String clazz) {
        super(project);
        this.project = project;
        this.clazz = clazz;

        setTitle(title);

        component = new JPanel(){{
            setLayout(new BorderLayout());
            add(tabbedPane = new JBTabbedPane(){{
                addTab("Project", page_project = new ListPage());
                addTab("All", page_all = new ListPage());
                addChangeListener(changeEvent -> {
                    if(getSelectedComponent() == page_project && !page_project.loaded)
                        loadProjectTab();
                    if(getSelectedComponent() == page_all && !page_all.loaded)
                        loadAllTab();
                });
            }});
        }};

        component.setPreferredSize(new Dimension(450, 250));

        loadProjectTab();
        init();
    }
    public void addBlackListClass(Class<?> clazz){
        addBlackListClass(clazz.getCanonicalName());
    }
    public void addBlackListClass(String class_path){
        blackList.add(class_path);
    }

    protected JComponent createCenterPanel() {
        return component;
    }

    public PsiClass getPsiClass(){
        show();
        if(isOK()){
            if(tabbedPane.getSelectedComponent() == page_project)
                return page_project.list.getSelectedValue();
            if(tabbedPane.getSelectedComponent() == page_all)
                return page_all.list.getSelectedValue();
        }
        return null;
    }

    void loadProjectTab(){
        page_project.loaded = true;

        final ArrayList<PsiClass> found = new ArrayList<>();
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            ApplicationManager.getApplication().runReadAction(() -> {
                Tools.getExtendedClassesInProject(project, clazz, psiClass -> {
                    if(psiClass.getClassKind().equals(JvmClassKind.CLASS) && !PsiUtil.isAbstractClass(psiClass) && !blackList.contains(psiClass.getQualifiedName())){
                        found.add(psiClass);
                        page_project.list.setListData(found.toArray(new PsiClass[0]));
                    }
                });
            });
        });
    }

    void loadAllTab(){
        page_all.loaded = true;

        final ArrayList<PsiClass> found = new ArrayList<>();
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            ApplicationManager.getApplication().runReadAction(() -> {
                Tools.getExtendedClassesInLibraries(project, clazz, psiClass -> {
                    if(psiClass.getClassKind().equals(JvmClassKind.CLASS) && !PsiUtil.isAbstractClass(psiClass) && !blackList.contains(psiClass.getQualifiedName())){
                        found.add(psiClass);
                        page_all.list.setListData(found.toArray(new PsiClass[0]));
                    }
                });
            });
        });
    }

    private class ListPage extends JPanel{

        public JBList<PsiClass> list;
        public boolean loaded = false;

        public ListPage(){
            setLayout(new BorderLayout());

            CollectionListModel<PsiClass> classes = new CollectionListModel<>();
            list = new JBList<>(classes);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setCellRenderer(new DefaultPsiElementCellRenderer());
            new DoubleClickListener(){
                protected boolean onDoubleClick(MouseEvent event) {
                    if (list.getSelectedValuesList().size() > 0) {
                        doOKAction();
                        return true;
                    }
                    return false;
                }
            }.installOn(list);

            ToolbarDecorator decorator = ToolbarDecorator.createDecorator(list);
            decorator.disableAddAction();
            decorator.disableRemoveAction();
            decorator.disableUpDownActions();

            add(decorator.createPanel());
        }

        public void setListData(PsiClass[] data){
            list.setListData(data);
        }
    }

}
