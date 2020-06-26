package com.husker.weblafplugin.extension.components.list.extend;

import com.husker.weblafplugin.core.components.list.FileList;
import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;

public class ExtendsList extends FileList<String> {

    private final SimpleXmlParameterEditor editor;

    public ExtendsList(SimpleXmlParameterEditor editor) {
        super();
        this.editor = editor;
        //setCellRenderer(new IncludeListCellRenderer());
        setDragEnabled(true);
    }

    protected void updateCachedData(){
    }

    protected boolean haveError(String element) {
        return false;
    }

    public SimpleXmlParameterEditor getSkinEditor(){
        return editor;
    }
}
