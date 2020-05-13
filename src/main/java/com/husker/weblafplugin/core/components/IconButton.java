package com.husker.weblafplugin.core.components;


import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ActionButtonLook;
import com.intellij.openapi.actionSystem.impl.IdeaActionButtonLook;
import com.intellij.openapi.actionSystem.impl.Win10ActionButtonLook;
import com.intellij.util.ui.ImageUtil;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class IconButton extends JComponent implements ActionButtonComponent {

    private ActionButtonLook look = UIUtil.isUnderWin10LookAndFeel() ? new Win10ActionButtonLook() : new IdeaActionButtonLook();
    private int state = NORMAL;

    private final ArrayList<ActionListener> listeners = new ArrayList<>();
    private Icon icon;
    private Icon disabled_icon;

    public IconButton(Icon icon){
        this();
        setIcon(icon);
    }

    public IconButton(Icon icon, Icon disabled){
        this();
        setIcon(icon);
        setDisabledIcon(disabled);
    }

    public IconButton() {
        setMinimumSize(new Dimension(25, 25));
        setPreferredSize(getMinimumSize());
        addMouseListener(new MouseAdapter() {
            boolean pressed = false;
            boolean hovered = false;
            public void mouseReleased(MouseEvent e) {
                if(hovered && isEnabled()){
                    for(ActionListener listener : listeners)
                        listener.actionPerformed(new ActionEvent(this, MouseEvent.BUTTON1, "pressed"));
                }
                pressed = false;
                updateState();
            }
            public void mousePressed(MouseEvent e) {
                pressed = true;
                updateState();
            }
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                updateState();
            }
            public void mouseExited(MouseEvent e) {
                hovered = false;
                updateState();
            }
            public void updateState(){
                if(hovered){
                    if(pressed)
                        state = PUSHED;
                    else
                        state = SELECTED;
                }else
                    state = NORMAL;
                repaint();
            }
        });
    }

    public void paintChildren(Graphics g) {}

    protected void paintButtonLook(Graphics g) {
        if(isEnabled()){
            look.paintBackground(g, this);

            if(getIcon() != null)
                look.paintIcon(g, this, getIcon());
            look.paintBorder(g, this);
        } else {
            look.paintIcon(g, this, getDisabledIcon());
        }

    }

    public void updateUI(){
        look = UIUtil.isUnderWin10LookAndFeel() ? new Win10ActionButtonLook() : new IdeaActionButtonLook();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        paintButtonLook(g);
    }

    public int getPopState() {
        return state;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Icon getDisabledIcon() {
        if(disabled_icon == null && getIcon() != null)
            return getDarkerIcon(getIcon());
        return disabled_icon;
    }

    public void setDisabledIcon(Icon icon) {
        this.disabled_icon = icon;
    }

    public void addActionListener(ActionListener listener){
        listeners.add(listener);
    }

    public Icon getDarkerIcon(Icon icon){
        BufferedImage image = ImageUtil.createImage(getWidth(), getHeight(), TYPE_INT_ARGB);
        icon.paintIcon(null, image.getGraphics(), (getWidth() - getIcon().getIconWidth()) / 2, (getHeight() - getIcon().getIconHeight()) / 2);

        WritableRaster wr = image.getRaster();
        int[] pixel = new int[4];
        for(int i = 0; i < wr.getWidth(); i++){
            for(int j = 0; j < wr.getHeight(); j++){
                wr.getPixel(i, j, pixel);
                float percent = UIUtil.isUnderDarcula() ? 0.6f : 1.6f;
                pixel[0] = (int) (pixel[0] * percent);
                pixel[1] = (int) (pixel[1] * percent);
                pixel[2] = (int) (pixel[2] * percent);
                wr.setPixel(i, j, pixel);
            }
        }

        BufferedImage new_image = ImageUtil.createImage(getWidth(), getHeight(), TYPE_INT_ARGB);
        new_image.getGraphics().drawImage(image, 0, 0, null);

        return new ImageIcon(new_image);
    }

}
