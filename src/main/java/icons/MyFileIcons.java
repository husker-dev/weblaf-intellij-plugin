package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public interface MyFileIcons {
    Icon DARK_STYLE = IconLoader.getIcon("/dark/style.png");
    Icon DARK_SKIN = IconLoader.getIcon("/dark/skin.png");
    Icon DARK_SKIN_AND_STYLE = IconLoader.getIcon("/dark/skin-and-style.png");

    Icon LIGHT_STYLE = IconLoader.getIcon("/light/style.png");
    Icon LIGHT_SKIN = IconLoader.getIcon("/light/skin.png");
    Icon LIGHT_SKIN_AND_STYLE = IconLoader.getIcon("/light/skin-and-style.png");
}
