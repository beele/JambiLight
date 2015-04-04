package be.beeles_place.jambiLight.commanding.events;

import java.util.function.Consumer;

public abstract class BaseEvent {

    private Runnable callback;
    private Consumer<String[]> errorCallback;

    //Getters & setters:
    public Runnable getCallback() {
        return callback;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    public Consumer<String[]> getErrorCallback() {
        return errorCallback;
    }

    public void setErrorCallback(Consumer<String[]> errorCallback) {
        this.errorCallback = errorCallback;
    }
}