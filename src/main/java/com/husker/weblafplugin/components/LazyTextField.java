package com.husker.weblafplugin.components;



import com.husker.weblafplugin.tools.Tools;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.components.JBTextField;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;

public class LazyTextField extends JBTextField {

    protected ArrayList<TextChangedListener> listeners = new ArrayList<>();
    protected boolean events_enabled = true;
    protected Thread last_thread;

    public LazyTextField(){
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

                if(last_thread != null && last_thread.isAlive()) {
                    last_thread.interrupt();
                    Tools.waitForThread(last_thread);
                }

                last_thread = new Thread(() -> {
                    try {
                        int delay = 500;

                        Thread.sleep(delay);

                        ApplicationManager.getApplication().invokeLater(() -> {
                            for (TextChangedListener listener : listeners)
                                listener.event(LazyTextField.this);
                        });

                    } catch (Exception ignored) {
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
