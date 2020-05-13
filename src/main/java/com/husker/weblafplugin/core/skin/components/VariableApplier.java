package com.husker.weblafplugin.core.skin.components;

import com.husker.weblafplugin.core.skin.variables.ValueChangedListener;

public interface VariableApplier {

    void addValueChangedListener(ValueChangedListener listener);
    void setValue(Object value);
    void onInit();

}
