package be.beeles_place.utils;

import com.google.common.eventbus.EventBus;

/**
 * Simple wrapper for the guava EventBus.
 */
public class EventbusWrapper {

    private EventBus eventbus;
    private static EventbusWrapper instance = new EventbusWrapper();

    /**
     * Gets the EventBus instance.
     * @return The EventBus instance.
     */
    public static EventBus getInstance() {
        return instance.eventbus;
    }

    /**
     * Private constructor for singleton.
     */
    private EventbusWrapper() {
        eventbus = new EventBus();
    }
}
