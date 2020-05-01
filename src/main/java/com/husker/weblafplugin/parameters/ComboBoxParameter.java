package com.husker.weblafplugin.parameters;

import com.husker.weblafplugin.components.parameter.Parameter;
import com.husker.weblafplugin.tools.ComponentTools;
import com.husker.weblafplugin.variables.ValueChangedListener;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class ComboBoxParameter extends Parameter {

    protected JComboBox<String> comboBox;

    public ComboBoxParameter(String name) {
        super(name);

        add(comboBox = new JComboBox<>());
        ComponentTools.setWidth(comboBox, DEFAULT_WIDTH);
    }

    public void addValueChangedListener(ValueChangedListener listener) {
        comboBox.addItemListener(itemEvent -> {
            if (itemEvent.getStateChange() == ItemEvent.SELECTED)
                listener.changed(itemEvent.getItem().toString());
        });
    }

    public void onValueApplying(Object value) {
        if(value instanceof Config) {
            Config config = (Config) value;

            setComboBoxElements(config.getItems());
            comboBox.setSelectedIndex(config.getSelectedIndex());
        }
    }

    public void setComboBoxElements(String[] element){
        comboBox.removeAllItems();
        for(String item : element)
            comboBox.addItem(item);
    }

    public boolean haveErrors() {
        return false;
    }

    class Config {

        private int selected_index;
        private String[] items;

        public Config(){
            this(0, new String[]{""});
        }

        public Config(int selected_index, String[] items){
            this.selected_index = selected_index;
            this.items = items;
        }

        public void setSelectedIndex(int index){
            selected_index = index;
        }
        public void setItems(String[] items){
            this.items = items;
        }

        public int getSelectedIndex(){
            return selected_index;
        }
        public String[] getItems(){
            return items;
        }
    }
}
