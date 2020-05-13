package com.husker.weblafplugin.core.skin.variables;

public abstract class Variable {

    private boolean isSetterAvailable = true;

    public abstract void setValue(Object object);
    public abstract Object getValue();

    public void setSetterAvailable(boolean available){
        isSetterAvailable = available;
    }

    public boolean isSetterAvailable(){
        return isSetterAvailable;
    }
}
