package com.husker.weblafplugin.core.dialogs;

import com.husker.weblafplugin.core.components.IconButton;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.icons.AllIcons;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.lang.jvm.JvmClassKind;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.util.PsiUtil;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.AsyncProcessIcon;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ClassChooserDialog extends DialogWrapper {

    private final JPanel component;
    private final ArrayList<String> blackList = new ArrayList<>();
    private JBTabbedPane tabbedPane;

    public ClassChooserDialog(Project project, String title) {
        this(project, title, (String) null);
    }

    public ClassChooserDialog(Project project, String title, Class<?> clazz) {
        this(project, title, clazz.getName());
    }

    public ClassChooserDialog(Project project, String title, String clazz) {
        super(project);

        setTitle(title);

        component = new JPanel(){{
            setLayout(new BorderLayout());
            add(tabbedPane = new JBTabbedPane(){{
                addChangeListener(changeEvent -> {
                    if(getSelectedComponent() instanceof ClassListPage){
                        ClassListPage page = (ClassListPage)getSelectedComponent();
                        page.tryToLoad();
                    }
                });
            }});
        }};
        component.setPreferredSize(new Dimension(450, 250));

        // Init tabs
        addPage(new ClassListPage("Project", page -> {
            final ArrayList<PsiClass> found = new ArrayList<>();
            Tools.getExtendedClassesInProject(project, clazz, psiClass -> {
                if(psiClass.getClassKind().equals(JvmClassKind.CLASS) && !PsiUtil.isAbstractClass(psiClass) && !blackList.contains(psiClass.getQualifiedName())){
                    found.add(psiClass);
                    page.setListData(found.toArray(new PsiClass[0]));
                }
            });
        }));
        addPage(new ClassListPage("All", page -> {
            final ArrayList<PsiClass> found = new ArrayList<>();
            Tools.getExtendedClassesInLibraries(project, clazz, psiClass -> {
                if(psiClass.getClassKind().equals(JvmClassKind.CLASS) && !PsiUtil.isAbstractClass(psiClass) && !blackList.contains(psiClass.getQualifiedName())){
                    found.add(psiClass);
                    page.setListData(found.toArray(new PsiClass[0]));
                }
            });
        }));


        init();
    }

    private void addPage(ClassListPage page){
        tabbedPane.addTab(page.getTitle(), page);
        if(tabbedPane.getTabCount() == 1)
            page.tryToLoad();
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
            if(tabbedPane.getSelectedComponent() instanceof ClassListPage) {
                ClassListPage page = (ClassListPage) tabbedPane.getSelectedComponent();
                return page.getPsiClass();
            }
        }
        return null;
    }

    private class ClassListPage extends JPanel{

        private JBList<PsiClass> list;
        private PsiClass[] content;

        private AsyncProcessIcon loadingIcon;
        private JLabel loadingText;
        private IconButton reloadButton;
        private JTextField searchField;

        private String searchText = "";

        private String title;
        private boolean loaded = false;

        private Consumer<ClassListPage> onLoad;

        public ClassListPage(String title, Consumer<ClassListPage> onLoad){
            setBackground(Color.GREEN);
            this.onLoad = onLoad;
            this.title = title;
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(-5, -8, -5, -9));

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
            add(new JPanel(){{
                setPreferredSize(new Dimension(100, 61));
                setLayout(new BorderLayout());
                add(new JPanel(){{
                    setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                    setBorder(BorderFactory.createEmptyBorder(7, 0, 0, 0));
                    add(loadingIcon = new AsyncProcessIcon("class_searching"));
                    add(loadingText = new JLabel("Searching..."));
                }}, BorderLayout.EAST);
                add(new JPanel(){{
                    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
                    add(reloadButton = new IconButton(AllIcons.Javaee.UpdateRunningApplication){{
                        addActionListener(e -> reload());
                    }});
                }}, BorderLayout.WEST);
                add(new JPanel(){{
                    setLayout(new BorderLayout());
                    setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
                    add(searchField = new JTextField(){{
                        putClientProperty("JTextField.variant", "search");
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
                                searchText = getText();
                                updateSearch();
                            }
                        });
                    }});
                }}, BorderLayout.SOUTH);
            }}, BorderLayout.NORTH);
        }

        public String getTitle(){
            return title;
        }

        public void tryToLoad(){
            if(isLoaded())
                return;
            setLoaded(true);
            reload();
        }

        private void reload(){
            list.setListData(new PsiClass[0]);
            ApplicationManager.getApplication().executeOnPooledThread(() -> {
                ApplicationManager.getApplication().runReadAction(() -> {
                    setSearchActive(true);
                    onLoad.accept(this);
                    setSearchActive(false);
                });
            });
        }

        public PsiClass getPsiClass(){
            return list.getSelectedValue();
        }

        public void setListData(PsiClass[] data){
            content = data;
            updateSearch();
        }

        public JBList<PsiClass> getList(){
            return list;
        }

        public void setLoaded(boolean loaded){
            this.loaded = loaded;
        }

        public boolean isLoaded(){
            return loaded;
        }

        private void updateSearch(){
            if(searchText.isEmpty())
                list.setListData(content);
            else{
                ArrayList<PsiClass> found = new ArrayList<>();
                for(PsiClass clazz : content)
                    if (clazz.getQualifiedName().toLowerCase().contains(searchText.toLowerCase().trim()))
                        found.add(clazz);
                list.setListData(found.toArray(new PsiClass[0]));
            }
        }

        public void setSearchActive(boolean active){
            loadingIcon.setVisible(active);
            reloadButton.setEnabled(!active);
            if(active)
                loadingText.setText("Searching...");
            else
                loadingText.setText(list.getItemsCount() + " classes found");
        }
    }

}
