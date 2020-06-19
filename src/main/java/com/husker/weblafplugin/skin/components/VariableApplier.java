package com.husker.weblafplugin.skin.components;

import com.husker.weblafplugin.skin.variables.ValueChangedListener;

public interface VariableApplier {

    void addValueChangedListener(ValueChangedListener listener);
    void setValue(Object value);
    void onInit();

}
