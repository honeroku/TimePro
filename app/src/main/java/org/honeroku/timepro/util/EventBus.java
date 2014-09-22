package org.honeroku.timepro.util;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public enum EventBus {
    INSTANCE;

    private Bus bus = new Bus();

    private EventBus() {
    }

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public void register(Object subscriber) {
        bus.register(subscriber);
    }

    public void unregister(Object subscriber) {
        bus.unregister(subscriber);
    }

    public void post(Object event) {
        bus.post(event);
    }

    public void postOnMain(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                bus.post(event);
            }
        });
    }

}
