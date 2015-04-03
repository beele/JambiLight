package be.beeles_place.jambiLight.commanding.events;

public abstract class BaseEvent {

    private Runnable callback;

    //Getters & setters:
    public Runnable getCallback() {
        return callback;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }
}