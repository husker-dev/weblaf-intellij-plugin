package com.husker.weblafplugin.core.components.list;

import com.husker.weblafplugin.skin.core.IncludeElement;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.*;

import static com.intellij.ide.dnd.SmoothAutoScroller.installDropTargetAsNecessary;


public abstract class FileList<T> extends JBList<T> {

    private HashMap<String, HashMap<T, Object>> cached = new HashMap<>();

    private T[] content;

    public FileList() {
        super(new CollectionListModel<T>(){
            public void exchangeRows(int oldIndex, int newIndex) {
                T elementToMove = getElementAt(oldIndex);

                remove(oldIndex);
                add(newIndex, elementToMove);

                fireContentsChanged(this, oldIndex, newIndex);
            }
        });
    }

    public FileList(@NotNull ListModel<T> dataModel) {
        super(dataModel);
    }

    public FileList(@NotNull T... listData) {
        super(listData);
        setModel(new CollectionListModel<T>(){
            public void exchangeRows(int oldIndex, int newIndex) {
                T elementToMove = getElementAt(oldIndex);

                remove(oldIndex);
                add(newIndex, elementToMove);

                fireContentsChanged(this, oldIndex, newIndex);
            }
        });
    }

    public FileList(@NotNull Collection<? extends T> items) {
        super(items);
        setModel(new CollectionListModel<T>(){
            public void exchangeRows(int oldIndex, int newIndex) {
                T elementToMove = getElementAt(oldIndex);

                remove(oldIndex);
                add(newIndex, elementToMove);

                fireContentsChanged(this, oldIndex, newIndex);
            }
        });
    }

    public void setContent(T[] listData) {
        content = listData;

        if(duplicates(listData)) {
            getModel().replaceAll(Collections.emptyList());
            return;
        }

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
        return cached.get(tag).get(element);
    }

    public boolean testForError() {
        if(duplicates(content)) {
            getModel().replaceAll(Collections.emptyList());
            return true;
        }
        clearCache();
        updateCachedData();

        for(T element : content)
            if(haveError(element))
                return true;
        return false;
    }

    private boolean duplicates(final T[] elements) {
        if(elements == null)
            return false;
        Set<T> lump = new HashSet<>();
        for (T i : elements) {
            if (lump.contains(i))
                return true;
            lump.add(i);
        }
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
