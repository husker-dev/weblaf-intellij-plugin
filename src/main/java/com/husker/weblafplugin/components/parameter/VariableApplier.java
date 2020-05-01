package com.husker.weblafplugin.components.parameter;

import com.husker.weblafplugin.variables.ValueChangedListener;

public interface VariableApplier {

    void addValueChangedListener(ValueChangedListener listener);
    void setValue(Object value);

}
