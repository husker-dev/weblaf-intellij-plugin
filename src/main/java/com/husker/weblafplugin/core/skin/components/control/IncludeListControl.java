package com.husker.weblafplugin.core.skin.components.control;

import com.husker.weblafplugin.core.components.IconButton;
import com.husker.weblafplugin.core.components.control.DefaultListControl;
import com.husker.weblafplugin.core.skin.components.list.include.IncludeList;
import com.husker.weblafplugin.core.skin.IncludeElement;
import com.husker.weblafplugin.core.skin.IncludeSorting;
import com.intellij.icons.AllIcons;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

public class IncludeListControl extends DefaultListControl<IncludeElement> {

    public IncludeListControl(Supplier<IncludeElement> creator){
        this(new IncludeList(), creator);
    }

    public IncludeListControl(IncludeList list, Supplier<IncludeElement> creator) {
        super(list, creator);

        setPreferredSize(new Dimension(470, 300));

        addButton(new IconButton(AllIcons.ObjectBrowser.SortByType){{
            setToolTipText("Sort");
            addActionListener(e -> {
                ArrayList<IncludeElement> elements = new ArrayList<>(Arrays.asList(list.getContent()));
                elements = new ArrayList<>(new IncludeSorting().sort(elements));

                fireContentChangedEvent(elements.toArray(new IncludeElement[0]));
            });
        }});
    }
}
