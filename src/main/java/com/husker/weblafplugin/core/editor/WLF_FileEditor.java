package com.husker.weblafplugin.core.editor;

import com.husker.weblafplugin.core.WLF_TypeChecker;
import com.husker.weblafplugin.core.tools.Listeners;
import com.husker.weblafplugin.extension.ExtensionEditor;
import com.husker.weblafplugin.skin.SkinEditor;
import com.husker.weblafplugin.style.StyleEditor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WLF_FileEditor extends WLF_AbstractFileEditor {

    static private final HashMap<Class<? extends WLF_Editor>, EditorCompatibilityChecker> editors = new HashMap<>();

    static {
        editors.put(SkinEditor.class, WLF_TypeChecker::isSkin);
        editors.put(ExtensionEditor.class, WLF_TypeChecker::isExtension);
        editors.put(StyleEditor.class, ((project, file) ->
                WLF_TypeChecker.isStyle(project, file) ||
                WLF_TypeChecker.isSkin(project, file) ||
                WLF_TypeChecker.isExtension(project, file))
        );
    }

    private final JBTabbedPane tabbedPane;

    public WLF_FileEditor(Project project, VirtualFile file) {
        tabbedPane = new JBTabbedPane();
        tabbedPane.setTabComponentInsets(JBUI.emptyInsets());

        Listeners.selectedFileEditorChanged(project, event -> {
            if (event.getNewEditor() != null && event.getNewEditor().getClass() == WLF_FileEditor.class)
                updateEditorList(project, file);
        });
        updateEditorList(project, file);
    }

    private void updateEditorList(Project project, VirtualFile file){
        ApplicationManager.getApplication().invokeLater(() -> {

            HashMap<Class<? extends WLF_Editor>, WLF_Editor> containingClasses = new HashMap<>();
            ArrayList<WLF_Editor> sortedEditors = new ArrayList<>();

            String selectedTab = "";

            for(int i = 0; i < tabbedPane.getTabCount(); i++) {
                if (tabbedPane.getComponentAt(i) instanceof JBScrollPane) {
                    Component view = ((JBScrollPane) tabbedPane.getComponentAt(i)).getViewport().getView();
                    if(view instanceof WLF_Editor){
                        WLF_Editor editor = (WLF_Editor) view;

                        if(tabbedPane.getSelectedIndex() == i)
                            selectedTab = editor.getTitle();

                        if(!editors.get(editor.getClass()).test(project, file))
                            editor.dispose();
                        else
                            containingClasses.put(editor.getClass(), editor);
                    }
                }
            }


            for(Map.Entry<Class<? extends WLF_Editor>, EditorCompatibilityChecker> entry : editors.entrySet()){
                try {
                    WLF_Editor editor;
                    if(containingClasses.containsKey(entry.getKey()))
                        editor = containingClasses.get(entry.getKey());
                    else if(entry.getValue().test(project, file)){
                        editor = entry.getKey().getConstructor(Project.class, VirtualFile.class).newInstance(project, file);
                    }else
                        continue;

                    int preferredIndex = editor.getPreferredTabIndex(sortedEditors.toArray(new WLF_Editor[0]));

                    for(int i = sortedEditors.size(); i < preferredIndex; i++)
                        sortedEditors.add(i, null);
                    sortedEditors.add(preferredIndex, editor);

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            tabbedPane.removeAll();
            for (WLF_Editor editor : sortedEditors) {
                if (editor == null)
                    continue;

                tabbedPane.insertTab(
                        editor.getTitle(),
                        editor.getIcon(),
                        new JBScrollPane(editor) {{
                            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtil.getBoundsColor()));
                        }},
                        editor.getTip(),
                        tabbedPane.getTabCount());

                if(selectedTab.equals(editor.getTitle()))
                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
                editor.onUpdate();
            }
        });

    }

    public JComponent getComponent() {
        return tabbedPane;
    }

    public String getName() {
        return "WebLaF Editor";
    }

    public void dispose() {
        for(int i = 0; i < tabbedPane.getTabCount(); i++)
            if(tabbedPane.getTabComponentAt(i) instanceof WLF_Editor)
                ((WLF_Editor)tabbedPane.getTabComponentAt(i)).dispose();
    }

    interface EditorCompatibilityChecker{
        boolean test(Project project, VirtualFile file);
    }
}
