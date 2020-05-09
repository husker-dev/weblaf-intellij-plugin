package com.husker.weblafplugin.components;

import com.husker.weblafplugin.components.list.List;
import com.husker.weblafplugin.components.list.ListElement;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ListControl extends JPanel {

    private final List<?> list;
    private final JBScrollPane scroll;
    private IconButton add, remove, up, down;
    private JPanel buttonPanel;

    private final ArrayList<ListControlListener> listeners = new ArrayList<>();

    public ListControl(List list){
        setLayout(new BorderLayout());

        this.list = list;
        add(scroll = new JBScrollPane(this.list));
        scroll.setAutoscrolls(true);

        list.addElementReorderedListener((from, to) -> {
            for(ListControlListener listener : listeners)
                listener.onReorder(from, to);
        });
        list.addDragListener(() -> {
            Point point = list.getMousePosition(true);
            if(point != null) {
                int radius = 20;
                Rectangle r = new Rectangle(point.x - radius, point.y - radius, radius * 2, radius * 2);
                list.scrollRectToVisible(r);
            }
        });

        add(buttonPanel = new JPanel(){{
            setLayout(new VerticalFlowLayout(0, 0));

            add(add = new IconButton(AllIcons.General.Add){{
                setToolTipText("Add");
                addActionListener(e -> {
                    for(ListControlListener listener : listeners)
                        listener.onAdd();
                });
            }});
            add(remove = new IconButton(AllIcons.General.Remove){{
                setToolTipText("Remove");
                addActionListener(e -> {
                    for(ListControlListener listener : listeners)
                        listener.onRemove(list.getSelectedElement());
                });
            }});
            add(up = new IconButton(AllIcons.General.ArrowUp){{
                setToolTipText("Shift Up");
                addActionListener(e -> {
                    for(ListControlListener listener : listeners)
                        listener.onReorder(list.getSelectedIndex(), list.getSelectedIndex() - 1);
                });
            }});
            add(down = new IconButton(AllIcons.General.ArrowDown){{
                setToolTipText("Shift Down");
                addActionListener(e -> {
                    for(ListControlListener listener : listeners)
                        listener.onReorder(list.getSelectedIndex(), list.getSelectedIndex() + 2);
                });
            }});
        }}, BorderLayout.EAST);

        list.addElementSelectedListener(element -> updateButtons());
        updateButtons();
    }

    public void updateButtons(){
        boolean visible = list.getSelectedElement() != null;

        remove.setEnabled(visible);
        up.setEnabled(list.getSelectedIndex() != 0 && visible);
        down.setEnabled(list.getSelectedIndex() != (list.getElementCount() - 1) && visible);
    }

    public void addButton(IconButton button){
        buttonPanel.add(button);
    }

    public void addListControlListener(ListControlListener listener){
        listeners.add(listener);
    }

    public interface ListControlListener<T>{
        void onAdd();
        void onRemove(ListElement<T> element);
        void onReorder(int from, int to);
    }
}
