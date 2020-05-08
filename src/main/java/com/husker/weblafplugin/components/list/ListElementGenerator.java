package com.husker.weblafplugin.components.list;

public interface ListElementGenerator<T> {

    ListElement<T> generateListElement(T object);
}
