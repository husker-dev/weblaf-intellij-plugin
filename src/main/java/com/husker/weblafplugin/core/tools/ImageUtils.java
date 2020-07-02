package com.husker.weblafplugin.core.tools;

import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static Icon scale(Icon icon, int width, int height){
        return new ImageIcon(scale(iconToImage(icon), width, height));
    }

    public static Image scale(Image image, int width, int height){
        BufferedImage buffered = createBufferedImage(image.getWidth(null), image.getHeight(null));
        buffered.getGraphics().drawImage(image, 0, 0 , null);

        return buffered.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public static Image iconToImage(Icon icon){
        if (icon instanceof ImageIcon)
            return ((ImageIcon)icon).getImage();
        else {
            BufferedImage image = createBufferedImage(icon.getIconWidth(), icon.getIconHeight());
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }

    public static BufferedImage createBufferedImage(int width, int height){
        return UIUtil.createImage((Component) null, width, height, BufferedImage.TYPE_INT_ARGB);
    }
}
