package com.husker.weblafplugin.core.components.list.files;

import com.husker.weblafplugin.core.components.list.ListElement;
import com.intellij.openapi.application.ApplicationManager;

import javax.swing.*;
import java.awt.*;

import static com.husker.weblafplugin.core.components.list.List.ElementState.UNSELECTED;


public abstract class AbstractFileListElement<T> extends ListElement<T> {

    private final JLabel file_icon;
    protected boolean hasError = false;

    public AbstractFileListElement(T content) {
        super(content);

        // IconVariable
        addToLeft(file_icon = new JLabel(){{
            setVerticalAlignment(CENTER);
            setHorizontalAlignment(CENTER);
            setPreferredSize(new Dimension(24, 20));
        }});
        onNameLabelsInit();
    }

    public void setIcon(Icon icon){
        ApplicationManager.getApplication().invokeLater(() -> file_icon.setIcon(icon));
    }

    public void onError(boolean haveError) {
        hasError = haveError;
        ApplicationManager.getApplication().invokeLater(this::updateColors);
    }

    public Color getBackgroundColor() {
        if(getState() == UNSELECTED && hasError)
            return getErrorColor();
        else
            return super.getBackgroundColor();
    }

    public abstract void onNameLabelsInit();
    public abstract boolean testForExistence();
}
