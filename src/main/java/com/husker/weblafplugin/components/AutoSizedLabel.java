package com.husker.weblafplugin.components;

import javax.swing.*;
import java.awt.*;

public class AutoSizedLabel extends JLabel {

    boolean needToResize = true;

    public AutoSizedLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }

    public AutoSizedLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
    }

    public AutoSizedLabel(String text) {
        super(text);
    }

    public AutoSizedLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
    }

    public AutoSizedLabel(Icon image) {
        super(image);
    }

    public AutoSizedLabel() {
    }

    public void paintComponent(Graphics gr){
        super.paintComponent(gr);

        if(needToResize) {
            needToResize = false;
            int text_width = SwingUtilities.computeStringWidth(getGraphics().getFontMetrics(), getText());
            setPreferredSize(new Dimension(text_width, getPreferredSize().height));
        }
    }

    public void setText(String text) {
        super.setText(text);
        needToResize = true;
    }
}
