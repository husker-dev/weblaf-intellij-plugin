package com.husker.weblafplugin.skin.core.components;

import com.husker.weblafplugin.core.tools.ComponentTools;
import com.husker.weblafplugin.skin.core.managers.SkinEditorManager;
import com.husker.weblafplugin.skin.core.variables.ValueChangedListener;
import com.husker.weblafplugin.core.tools.Tools;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class IconViewer extends JLabel implements VariableApplier {

    public static final int DEFAULT_IMAGE_SIZE = 110;

    private int size;

    public IconViewer(int size){
        this.size = size;
    }
    public IconViewer(){
       this(DEFAULT_IMAGE_SIZE);
    }

    public void onInit(){
        ComponentTools.setSize(this, size, size);
    }

    public void addValueChangedListener(ValueChangedListener listener) {

    }

    public String getResourcePath(){
        return SkinEditorManager.get(this).Resources.getResourcePath();
    }

    public void setValue(Object value) {
        try {

            BufferedImage img = ImageIO.read(Tools.getVirtualFile(getResourcePath() + "/" + value).getInputStream());
            Image resized = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);

            setIcon(new ImageIcon(resized));
        }catch (Exception ex){
            setIcon(null);
        }
    }
}
