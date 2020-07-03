package com.husker.weblafplugin.core.components.list;

import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.openapi.project.Project;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.DoubleClickListener;
import com.intellij.ui.ExpandedItemListCellRendererWrapper;
import com.intellij.ui.components.JBList;
import javafx.scene.control.ListCell;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.*;

import static com.intellij.ide.dnd.SmoothAutoScroller.installDropTargetAsNecessary;


public abstract class FileList<T> extends JBList<T> {

    private HashMap<String, HashMap<T, Object>> cached = new HashMap<>();

    private T[] content;
    private Project project;

    private boolean doubleClickEnable = true;

    public FileList(Project project) {
        super(getListModel());
        init(project);
    }

    private void init(Project project){
        this.project = project;
        super.setModel(new CollectionListModel<T>(){
            public void exchangeRows(int oldIndex, int newIndex) {
                T elementToMove = getElementAt(oldIndex);

                remove(oldIndex);
                add(newIndex, elementToMove);

                fireContentsChanged(this, oldIndex, newIndex);
            }
        });
        new DoubleClickListener(){
            protected boolean onDoubleClick(MouseEvent event) {
                if(doubleClickEnable)
                    Tools.openFile(project, Tools.getVirtualFile(getCellRendererForElement(getSelectedValue()).getFilePath()));
                return doubleClickEnable;
            }
        }.installOn(this);
    }

    private static <T> CollectionListModel<T> getListModel(){
        return new CollectionListModel<T>(){
            public void exchangeRows(int oldIndex, int newIndex) {
                T elementToMove = getElementAt(oldIndex);

                remove(oldIndex);
                add(newIndex, elementToMove);

                fireContentsChanged(this, oldIndex, newIndex);
            }
        };
    }

    private FileCellRenderer<T> getCellRendererForElement(T element){
        getFileCellRenderer().getListCellRendererComponent(this, element, getListModel().getElementIndex(element), true, true);
        return getFileCellRenderer();
    }

    public FileCellRenderer<T> getFileCellRenderer() {
        ListCellRenderer<? super T> renderer = super.getCellRenderer();
        if(renderer instanceof ExpandedItemListCellRendererWrapper){
            ExpandedItemListCellRendererWrapper<? super T> wrapper = (ExpandedItemListCellRendererWrapper<? super T>) renderer;
            if(wrapper.getWrappee() instanceof FileCellRenderer)
                return (FileCellRenderer<T>) wrapper.getWrappee();
            else
                return null;
        }
        if(renderer instanceof FileCellRenderer)
            return (FileCellRenderer<T>) super.getCellRenderer();
        else
            return null;
    }

    public Project getProject(){
        return project;
    }

    public void setContent(T[] listData) {
        content = listData;

        clearCache();
        updateCachedData();

        getModel().replaceAll(Arrays.asList(listData));
    }

    public void setTransferHandler(TransferHandler handler) {
        installDropTargetAsNecessary(this);
        super.setTransferHandler(handler);
    }

    public void cache(String tag, T element, Object object){
        if(!cached.containsKey(tag))
            cached.put(tag, new HashMap<>());
        cached.get(tag).put(element, object);
    }

    public void clearCache(){
        cached.clear();
    }

    public Object getCached(String tag, T element){
        if(!cached.containsKey(tag))
            return null;
        if(!cached.get(tag).containsKey(element))
            return null;
        return cached.get(tag).get(element);
    }

    public boolean testForError() {
        clearCache();
        updateCachedData();

        for(T element : content)
            if(haveError(element))
                return true;
        return false;
    }

    public CollectionListModel<T> getModel(){
        return (CollectionListModel<T>) super.getModel();
    }

    public T[] getContent(){
        return content;
    }

    protected abstract void updateCachedData();
    protected abstract boolean haveError(T element);

}
