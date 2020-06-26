package com.husker.weblafplugin.core.managers;


import com.husker.weblafplugin.skin.components.VariableApplier;
import com.husker.weblafplugin.skin.variables.Variable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

public class ParameterManager {

    private static ArrayList<Container> containers = new ArrayList<>();
    private static HashMap<VariableApplier, Variable> variables = new HashMap<>();
    private static HashMap<Container, ArrayList<Consumer<Container>>> reload_listeners = new HashMap<>();

    public static void markAsParameterContainer(Container container){
        containers.add(container);
    }

    public static void register(VariableApplier applier, Variable variable){
        variables.put(applier, variable);
    }

    public static void init(Component context){
        Container parent = getParentParameterContainer(context);

        for(VariableApplier parameterInstance : getChildParameters(parent)) {
            parameterInstance.onInit();

            parameterInstance.addValueChangedListener((value -> {
                if(variables.get(parameterInstance).isSetterAvailable()) {
                    variables.get(parameterInstance).setValue(value);
                    reloadVariables((Component) parameterInstance);
                }
            }));
        }
    }

    public static void reloadVariables(Component context){
        Container parent = getParentParameterContainer(context);

        if(reload_listeners.get(parent) != null)
            for(Consumer<Container> listener : reload_listeners.get(parent))
                listener.accept(parent);

        for(VariableApplier parameterInstance : getChildParameters(parent)) {
            if(variables.containsKey(parameterInstance)) {
                try {
                    variables.get(parameterInstance).setSetterAvailable(false);
                    parameterInstance.setValue(variables.get(parameterInstance).getValue());
                    variables.get(parameterInstance).setSetterAvailable(true);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public static Container getParentParameterContainer(Component component){
        if(component instanceof Container && containers.contains(component))
            return (Container) component;

        Container current_parent = component.getParent();
        while(true){
            if(current_parent == null || containers.contains(current_parent))
                return current_parent;
            else
                current_parent = current_parent.getParent();
        }
    }

    public static VariableApplier[] getChildParameters(Container container){
        ArrayList<VariableApplier> parameters = new ArrayList<>();
        for(Component component : getChildComponents(container))
            if(component instanceof VariableApplier)
                parameters.add((VariableApplier) component);
        return parameters.toArray(new VariableApplier[0]);
    }

    private static Component[] getChildComponents(Container container){
        ArrayList<Component> components = new ArrayList();
        for(Component child : container.getComponents()){
            components.add(child);
            if(child instanceof Container)
                components.addAll(Arrays.asList(getChildComponents((Container) child)));
        }
        return components.toArray(new Component[0]);
    }

    public static void addReloadListener(Container container, Consumer<Container> listener){
        reload_listeners.computeIfAbsent(container, k -> new ArrayList<>());
        reload_listeners.get(container).add(listener);
    }

    public static void disposeContainer(Container container){
        containers.remove(container);
        reload_listeners.remove(container);
        for(VariableApplier applier : getChildParameters(container))
            variables.remove(applier);
    }

}
