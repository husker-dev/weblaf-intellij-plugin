package com.husker.weblafplugin.core.components.control;

import com.husker.weblafplugin.core.components.list.List;
import com.husker.weblafplugin.core.components.list.ListElement;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DefaultListControl<T> extends ListControl<T> {

    private final Supplier<T> elementCreator;
    private final ArrayList<Consumer<T[]>> listeners = new ArrayList<>();

    public DefaultListControl(List<T> list, Supplier<T> creator) {
        super(list);
        this.elementCreator = creator;

        setPreferredSize(new Dimension(470, 200));

        addListControlListener(new ListControlListener<T>() {
            public void onAdd() {
                T object = elementCreator.get();

                if(object != null){
                    ArrayList<T> elements = new ArrayList<>(Arrays.asList(list.getContent()));
                    elements.add(object);

                    fireContentChangedEvent(toArray(elements));
                }
            }

            public void onRemove(ListElement<T> element) {
                ArrayList<T> elements = new ArrayList<>(Arrays.asList(list.getContent()));
                elements.remove(element.getContent());

                fireContentChangedEvent(toArray(elements));
            }

            public void onReorder(int from, int to) {
                try {
                    ArrayList<T> elements = new ArrayList<>(Arrays.asList(list.getContent()));
                    elements.set(from, null);

                    elements.add(to, list.getContentAt(from));
                    elements.remove(null);

                    fireContentChangedEvent(toArray(elements));
                }catch (Exception ignored){}
            }
        });
    }

    public T[] toArray(ArrayList<T> list) {
        if(list.size() == 0)
            return null;

        T[] stackArray = (T[]) Array.newInstance(list.get(0).getClass(), list.size());
        for (int i = 0; i < list.size(); i++)
            stackArray[i] = list.get(i);
        return stackArray;
    }

    public void fireContentChangedEvent(T[] array){
        for(Consumer<T[]> listener : listeners)
            listener.accept(array);
    }

    public void addContentChangedListener(Consumer<T[]> listener){
        listeners.add(listener);
    }

}
