package com.husker.weblafplugin.skin.core.components;

import com.husker.weblafplugin.skin.core.variables.ValueChangedListener;

public interface VariableApplier {

    void addValueChangedListener(ValueChangedListener listener);
    void setValue(Object value);
    void onInit();

}
