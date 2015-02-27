package be.beeles_place.jambiLight.utils.commanding.events;

public abstract class BaseEvent<T> {

    private T payload;

    //Getters & setters:
    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}