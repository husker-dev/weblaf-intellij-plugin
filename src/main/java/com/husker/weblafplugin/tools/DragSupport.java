package com.husker.weblafplugin.tools;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;

public class DragSupport {

    public DragSupport(Component component, Object object){
        Transferable transferable = new Transferable() {
            public final DataFlavor FOLDER_FLAVOR = new DataFlavor(object.getClass(), object.getClass().getSimpleName());

            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{
                        FOLDER_FLAVOR
                };
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return FOLDER_FLAVOR.equals(flavor);
            }

            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                if (flavor.equals(FOLDER_FLAVOR))
                    return object;
                else
                    throw new UnsupportedFlavorException(flavor);
            }
        };

        DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(component, DnDConstants.ACTION_MOVE, drg -> drg.startDrag(null, transferable, null));
    }
}
