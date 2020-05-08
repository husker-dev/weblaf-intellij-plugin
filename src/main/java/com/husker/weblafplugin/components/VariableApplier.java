package com.husker.weblafplugin.components;

import com.husker.weblafplugin.variables.ValueChangedListener;

public interface VariableApplier {

    void addValueChangedListener(ValueChangedListener listener);
    void setValue(Object value);

}
