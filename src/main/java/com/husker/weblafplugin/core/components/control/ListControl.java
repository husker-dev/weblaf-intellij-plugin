package com.husker.weblafplugin.core.components.control;

import com.husker.weblafplugin.core.components.IconButton;
import com.husker.weblafplugin.core.components.list.List;
import com.husker.weblafplugin.core.components.list.ListElement;
import com.husker.weblafplugin.core.components.scroll.TransparentScrollPane;
import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.VerticalFlowLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ListControl<T> extends JPanel {

    private final List<T> list;
    private final TransparentScrollPane scroll;
    private IconButton add, remove, up, down;
    private JPanel buttonPanel;

    private final ArrayList<ListControlListener<T>> listeners = new ArrayList<>();

    public ListControl(List<T> list){
        setLayout(new BorderLayout());
        this.list = list;

        scroll = new TransparentScrollPane(this.list);
        add(scroll);

        list.addElementReorderedListener((from, to) -> {
            for(ListControlListener<T> listener : listeners)
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
                    for(ListControlListener<T> listener : listeners)
                        listener.onAdd();
                });
            }});
            add(remove = new IconButton(AllIcons.General.Remove){{
                setToolTipText("Remove");
                addActionListener(e -> {
                    for(ListControlListener<T> listener : listeners)
                        listener.onRemove(list.getSelectedElement());
                });
            }});
            add(up = new IconButton(AllIcons.General.ArrowUp){{
                setToolTipText("Move Up");
                addActionListener(e -> {
                    for(ListControlListener<T> listener : listeners)
                        listener.onReorder(list.getSelectedIndex(), list.getSelectedIndex() - 1);
                });
            }});
            add(down = new IconButton(AllIcons.General.ArrowDown){{
                setToolTipText("Move Down");
                addActionListener(e -> {
                    for(ListControlListener<T> listener : listeners)
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

    public void addListControlListener(ListControlListener<T> listener){
        listeners.add(listener);
    }

    public List<T> getList(){
        return list;
    }

    public interface ListControlListener<A>{
        void onAdd();
        void onRemove(ListElement<A> element);
        void onReorder(int from, int to);
    }
}
