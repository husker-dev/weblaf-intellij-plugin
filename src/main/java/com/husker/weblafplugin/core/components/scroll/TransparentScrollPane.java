package com.husker.weblafplugin.core.components.scroll;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.util.Key;
import com.intellij.ui.GuiUtils;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBViewport;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TransparentScrollPane extends JBScrollPane {

    public TransparentScrollPane(int viewportWidth) {
        super(viewportWidth);
        init();
    }

    public TransparentScrollPane() {
        init();
    }

    public TransparentScrollPane(Component view) {
        super(view);
        init();
    }

    public TransparentScrollPane(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
        init();
    }

    public TransparentScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        init();
    }

    private void init(){
        UIUtil.putClientProperty((JComponent)getViewport().getView(), Key.create("forceVisibleRowCount"), true);

        /*
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                updateContent();
            }
            public void componentMoved(ComponentEvent e) {
                updateContent();
            }
            public void componentShown(ComponentEvent e) {
                updateContent();
            }
            public void componentHidden(ComponentEvent e) {
                updateContent();
            }
        });

         */
    }

    private void updateContent(){
        JComponent component = (JComponent) getViewport().getView();
        if(getVerticalScrollBar().isShowing())
            component.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, -14));
        else
            component.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        if(component instanceof ScrollFitApplier)
            ((ScrollFitApplier)component).fitContent(getVerticalScrollBar().isShowing());
    }
}
