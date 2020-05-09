package com.husker.weblafplugin.parameters;

import com.husker.weblafplugin.components.IconButton;
import com.husker.weblafplugin.components.ListControl;
import com.husker.weblafplugin.components.list.ListElement;
import com.husker.weblafplugin.components.list.include.IncludeElement;
import com.husker.weblafplugin.components.parameter.Parameter;
import com.husker.weblafplugin.components.list.include.IncludeList;
import com.husker.weblafplugin.tools.IncludeSorting;
import com.husker.weblafplugin.variables.ValueChangedListener;
import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class IncludeListParameter extends Parameter {

    protected IncludeList list;

    protected ArrayList<ValueChangedListener> listeners = new ArrayList<>();

    public IncludeListParameter(String name) {
        super(name);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        panel.add(new ListControl(list = new IncludeList()){{
            setPreferredSize(new Dimension(470, 400));

            addButton(new IconButton(AllIcons.ObjectBrowser.SortByType){{
                setToolTipText("Sort");
                addActionListener(e -> {
                    ArrayList<IncludeElement> elements = new ArrayList<>(Arrays.asList(list.getContent()));
                    elements = new ArrayList<>(new IncludeSorting().sort(elements));

                    for(ValueChangedListener listener : listeners)
                        listener.changed(elements.toArray(new IncludeElement[0]));
                });
            }});
            addListControlListener(new ListControlListener<IncludeElement>() {
                public void onAdd() {

                }
                public void onRemove(ListElement<IncludeElement> listElement) {
                    ArrayList<IncludeElement> elements = new ArrayList<>(Arrays.asList(list.getContent()));
                    elements.remove(listElement.getContent());

                    for(ValueChangedListener listener : listeners)
                        listener.changed(elements.toArray(new IncludeElement[0]));
                }
                public void onReorder(int from, int to) {
                    try {
                        ArrayList<IncludeElement> elements = new ArrayList<>(Arrays.asList(list.getContent()));
                        IncludeElement buffer = new IncludeElement(null, "", "buffer.xml", "");
                        elements.set(from, buffer);

                        elements.add(to, list.getContentAt(from));
                        elements.remove(buffer);

                        for (ValueChangedListener listener : listeners)
                            listener.changed(elements.toArray(new IncludeElement[0]));
                    }catch (Exception ex){}
                }
            });
        }});
        panel.setBorder(BorderFactory.createEmptyBorder(5, 3, 0, 0));
        add(panel);
    }

    public void addValueChangedListener(ValueChangedListener listener) {
        listeners.add(listener);
    }

    public void onValueApplying(Object value) {
        list.setContent((IncludeElement[]) value);
    }

    public boolean haveErrors() {
        list.setResourcePath(getSkinEditor().getResourcePath());
        list.update();
        return false;
    }

}
