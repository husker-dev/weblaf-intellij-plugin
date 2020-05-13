package com.husker.weblafplugin.core.components.list;

public interface ListElementGenerator<T> {

    ListElement<T> generateListElement(T object);
}
