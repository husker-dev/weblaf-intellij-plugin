package com.husker.weblafplugin.core.tools;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.util.ArrayList;

public class DropSupport {

    private final ArrayList<ObjectDroppedListener> dropped_listeners = new ArrayList<>();
    private final ArrayList<HoverListener> hovered_listeners = new ArrayList<>();
    private final ArrayList<MoveListener> moved_listeners = new ArrayList<>();
    private boolean isHovered = false;

    public DropSupport(Component component, Class<?> clazz){
        new DropTarget(component, DnDConstants.ACTION_MOVE, new DropTargetAdapter() {
            public final DataFlavor FOLDER_FLAVOR = new DataFlavor(clazz, clazz.getSimpleName());

            public void dragEnter(DropTargetDragEvent event) {
                isHovered = true;
                if (event.getTransferable().isDataFlavorSupported(FOLDER_FLAVOR)) {
                    for(HoverListener listener : hovered_listeners)
                        listener.changed(true);
                }
            }
            public void dragOver(DropTargetDragEvent event) {
                for(MoveListener listener : moved_listeners)
                    listener.moved();
            }
            public void dropActionChanged(DropTargetDragEvent event) {
            }
            public void dragExit(DropTargetEvent event) {
                isHovered = false;
                for(HoverListener listener : hovered_listeners)
                    listener.changed(false);
            }
            public void drop(DropTargetDropEvent event) {
                if (event.getTransferable().isDataFlavorSupported(FOLDER_FLAVOR)) {
                    try {
                        Object transferData = event.getTransferable().getTransferData(FOLDER_FLAVOR);
                        event.acceptDrop(DnDConstants.ACTION_MOVE);

                        for(ObjectDroppedListener listener : dropped_listeners)
                            listener.dropped(transferData);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        event.rejectDrop();
                    }
                }else
                    event.rejectDrop();
            }
        });
    }

    public boolean isHovered(){
        return isHovered;
    }

    public void addDropListener(ObjectDroppedListener listener){
        dropped_listeners.add(listener);
    }
    public void addHoverListener(HoverListener listener){
        hovered_listeners.add(listener);
    }
    public void addMoveListener(MoveListener listener){
        moved_listeners.add(listener);
    }

    public interface ObjectDroppedListener{
        void dropped(Object object);
    }
    public interface HoverListener{
        void changed(boolean hovered);
    }
    public interface MoveListener{
        void moved();
    }
}
