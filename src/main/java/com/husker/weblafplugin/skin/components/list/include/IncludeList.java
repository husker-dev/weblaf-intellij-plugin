package com.husker.weblafplugin.skin.components.list.include;

import com.husker.weblafplugin.core.components.list.FileList;
import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.skin.include.IncludeElement;
import com.husker.weblafplugin.skin.managers.SkinEditorManager;

import javax.swing.*;

public class IncludeList extends FileList<IncludeElement> {

    private final SimpleXmlParameterEditor editor;

    public IncludeList(SimpleXmlParameterEditor editor) {
        super(editor.getProject());
        this.editor = editor;
        setCellRenderer(new IncludeListCellRenderer());
        setDragEnabled(true);
    }

    protected void updateCachedData(){
        String path = SkinEditorManager.Resources.getResourcePath(editor);
        for (IncludeElement element : getContent()) {
            element.setResourcePath(path);
            cache("errors", element, element.isExist());
        }
        for (IncludeElement element : getContent())
            cache("icons", element, element.getIcon());
    }

    protected boolean haveError(IncludeElement element) {
        return !isExist(element);
    }

    public boolean isExist(IncludeElement element){
        return (Boolean) getCached("errors", element);
    }
    public Icon getIcon(IncludeElement element){
        return (Icon) getCached("icons", element);
    }

    public SimpleXmlParameterEditor getSkinEditor(){
        return editor;
    }
}
