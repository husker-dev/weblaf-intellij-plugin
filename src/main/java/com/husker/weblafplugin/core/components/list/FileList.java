package com.husker.weblafplugin.core.components.list;

import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.openapi.project.Project;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.DoubleClickListener;
import com.intellij.ui.ExpandedItemListCellRendererWrapper;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.*;

import static com.intellij.ide.dnd.SmoothAutoScroller.installDropTargetAsNecessary;


public abstract class FileList<T> extends JBList<T> {

    private HashMap<String, HashMap<T, Object>> cached = new HashMap<>();

    private T[] content;
    private Project project;

    private boolean doubleClickEnable = true;
    private boolean autoClearCache = true;

    public FileList(Project project) {
        super(createListModel());
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
                if(getSelectedValue() == null)
                    return false;
                if(doubleClickEnable)
                    Tools.openFile(project, Tools.getVirtualFile(getCellRendererForElement(getSelectedValue()).getFilePath()));
                return doubleClickEnable;
            }
        }.installOn(this);
    }

    public void setDoubleClickEnabled(boolean enabled){
        doubleClickEnable = enabled;
    }

    public void setAutoClearCacheEnabled(boolean enabled){
        autoClearCache = enabled;
    }

    private static <T> CollectionListModel<T> createListModel(){
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
        getFileCellRenderer().getListCellRendererComponent(this, element, createListModel().getElementIndex(element), true, true);
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

        if(autoClearCache)
            clearCache();
        else
            clearCachePartially(listData);
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

    public void clearCachePartially(List<T> elements){
        cached.entrySet().iterator().forEachRemaining(tagEntry -> {
            Iterator<Map.Entry<T, Object>> valueIterator = tagEntry.getValue().entrySet().iterator();
            valueIterator.forEachRemaining(valueEntry -> {
                if(!elements.contains(valueEntry.getKey()))
                    valueIterator.remove();
            });
        });
    }

    public void clearCachePartially(T[] elements){
        clearCachePartially(Arrays.asList(elements));
    }

    public Object getCached(String tag, T element){
        if(!cached.containsKey(tag))
            return null;
        if(!cached.get(tag).containsKey(element))
            return null;
        return cached.get(tag).get(element);
    }

    public boolean testForError() {
        if(autoClearCache)
            clearCache();
        else
            clearCachePartially(getModel().getItems());
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
