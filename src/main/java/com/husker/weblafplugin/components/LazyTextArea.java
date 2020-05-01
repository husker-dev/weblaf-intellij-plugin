package com.husker.weblafplugin.components;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.components.JBTextField;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;

public class LazyTextArea extends JBTextField {

    protected ArrayList<TextChangedListener> listeners = new ArrayList<>();
    protected long last_time_pressed = 0;
    protected boolean events_enabled = true;
    protected Thread last_thread;

    public LazyTextArea(){
        super();

        getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                event();
            }
            public void removeUpdate(DocumentEvent e) {
                event();
            }
            public void insertUpdate(DocumentEvent e) {
                event();
            }
            public void event() {
                if(!events_enabled)
                    return;

                last_time_pressed = System.currentTimeMillis();

                if(last_thread != null && last_thread.isAlive()) {
                    last_thread.interrupt();
                    while(last_thread.isAlive()){ }
                }

                last_thread = new Thread(() -> {
                    try {
                        int delay = 500;

                        Thread.sleep(delay);
                        if(System.currentTimeMillis() - last_time_pressed > delay) {
                            ApplicationManager.getApplication().invokeLater(() -> {
                                for (TextChangedListener listener : listeners)
                                    listener.event(LazyTextArea.this);
                            });
                        }
                    } catch (Exception e) {
                    }
                });
                last_thread.start();
            }
        });


    }

    public void addTextChangedListener(TextChangedListener listener){
        listeners.add(listener);
    }

    public void setEventsEnabled(boolean enabled){
        events_enabled = enabled;
    }
}
