package be.beeles_place.jambiLight.commanding.events.application;

public class VisualDebugEvent {

    private boolean isStart;

    public VisualDebugEvent(boolean isStart) {
        this.isStart = isStart;
    }

    //Getters & setters.
    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }
}
