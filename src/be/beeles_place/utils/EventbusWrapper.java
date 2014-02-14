package be.beeles_place.utils;

import com.google.common.eventbus.EventBus;

public class EventbusWrapper {

    private EventBus eventbus;
    private static EventbusWrapper instance = new EventbusWrapper();

    public static EventBus getInstance() {
        return instance.eventbus;
    }

    private EventbusWrapper() {
        eventbus = new EventBus();
    }
}
