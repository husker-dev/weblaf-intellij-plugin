package com.husker.weblafplugin.skin.core.parameters;

import com.husker.weblafplugin.skin.core.variables.ValueChangedListener;
import com.intellij.openapi.ui.VerticalFlowLayout;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SupportedSystemsParameter extends ComboBoxParameter {

    protected LinkedHashMap<String, String[]> os = new LinkedHashMap<String, String[]>(){{
        put("Windows", new String[]{"win"});
        put("Mac OS", new String[]{"mac", "darwin"});
        put("Linux", new String[]{"nix", "nux"});
        put("Solaris", new String[]{"sunos"});
    }};

    protected static final String ITEM_ALL = "All";
    protected static final String ITEM_CUSTOM = "Custom";

    protected JCheckBox[] checkBoxes = new JCheckBox[os.size()];
    protected JPanel content;

    public SupportedSystemsParameter(String name) {
        super(name);
    }

    public void onInit(){
        super.onInit();

        remove(comboBox);

        super.addValueChangedListener(value -> content.setVisible(ITEM_CUSTOM.equals(comboBox.getSelectedItem())));

        add(new JPanel(){{
            setLayout(new BorderLayout());

            add(comboBox, BorderLayout.NORTH);
            add(content = new JPanel(){{
                setLayout(new VerticalFlowLayout());

                int i = 0;
                for(Map.Entry<String, String[]> entry : os.entrySet()){
                    JCheckBox checkBox = new JCheckBox(entry.getKey());
                    checkBoxes[i] = checkBox;
                    add(checkBox);
                    i++;
                }
            }});
        }});
    }

    public void addValueChangedListener(ValueChangedListener listener) {
        super.addValueChangedListener(value -> listener.changed(getValue()));

        for(JCheckBox checkBox : checkBoxes)
            checkBox.addActionListener(event -> listener.changed(getValue()));
    }

    public void setValue(Object value) {
        if(value == null)
            super.setValue(new Config(1, new String[]{ITEM_ALL, ITEM_CUSTOM}));

        if(value instanceof String[]){
            String[] values = (String[])value;
            if(values.length > 0){
                if(values[0].equals("all")){
                    super.setValue(new Config(0, new String[]{ITEM_ALL, ITEM_CUSTOM}));
                }else{
                    super.setValue(new Config(1, new String[]{ITEM_ALL, ITEM_CUSTOM}));

                    // Setting up checkboxes
                    int i = 0;
                    for(Map.Entry<String, String[]> entry : os.entrySet()){
                        java.util.List<String> os_tags = Arrays.asList(entry.getValue());

                        boolean isSelected = false;
                        for(String val : values){
                            if(os_tags.contains(val)) {
                                isSelected = true;
                                break;
                            }
                        }

                        checkBoxes[i].setSelected(isSelected);
                        i++;
                    }
                }
            }
        }
    }

    protected String[] getValue(){
        ArrayList<String> out = new ArrayList<>();

        if(ITEM_ALL.equals(comboBox.getSelectedItem()))
            out.add("all");

        if(ITEM_CUSTOM.equals(comboBox.getSelectedItem())){
            for(JCheckBox checkBox : checkBoxes)
                if(checkBox.isSelected())
                    Collections.addAll(out, os.get(checkBox.getText()));
        }

        return out.toArray(new String[0]);
    }

    public boolean haveErrors() {
        if(ITEM_CUSTOM.equals(comboBox.getSelectedItem())) {
            boolean isOk = false;
            for (JCheckBox checkBox : checkBoxes)
                if (checkBox.isSelected())
                    isOk = true;
            return !isOk;
        }
        return false;
    }
}
