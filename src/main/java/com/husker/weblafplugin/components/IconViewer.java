package com.husker.weblafplugin.components;

import com.husker.weblafplugin.tools.SkinEditorManager;
import com.husker.weblafplugin.tools.ComponentTools;
import com.husker.weblafplugin.variables.ValueChangedListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class IconViewer extends JLabel implements VariableApplier {

    public static final int DEFAULT_IMAGE_SIZE = 110;

    private int size;

    public IconViewer(int size){
        this.size = size;
        ComponentTools.setSize(this, size, size);
    }
    public IconViewer(){
       this(DEFAULT_IMAGE_SIZE);
    }

    public void addValueChangedListener(ValueChangedListener listener) {

    }

    public String getResourcePath(){
        return SkinEditorManager.get(this).getResourcePath();
    }

    public void setValue(Object value) {
        try {
            BufferedImage img = ImageIO.read(new File(getResourcePath() + "/" + value));
            Image resized = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);

            setIcon(new ImageIcon(resized));
        }catch (Exception ex){
            setIcon(null);
        }
    }
}
