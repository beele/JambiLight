package be.beeles_place.model;

import be.beeles_place.events.ColorModelUpdatedEvent;
import be.beeles_place.utils.EventbusWrapper;
import com.google.common.eventbus.EventBus;

import java.awt.*;

public class ColorModel {

    private int[][] currentColors;
    private long actionDuration;
    private boolean newColorsForComm;

    private EventBus eventbus;
    private int numberOfColorsProcessed;

    public ColorModel() {
        eventbus = EventbusWrapper.getInstance();
        eventbus.register(this);

        newColorsForComm = false;
    }

    public void publishModelUpdate() {
        //Notify about the updated colors!
        eventbus.post(new ColorModelUpdatedEvent());
    }

    //Getters & setters.
    public int[][] getCurrentColors() {
        return currentColors;
    }

    public int[][]getCurrentColorsForComm() {
        newColorsForComm = false;
        return currentColors;
    }

    public void setCurrentColors(int[][] currentColors) {
        newColorsForComm = true;
        this.currentColors = currentColors;
    }

    public long getActionDuration() {
        return actionDuration;
    }

    public void setActionDuration(long actionDuration) {
        this.actionDuration = actionDuration;
    }

    public void setNumberOfColorsProcessed(int numberOfColorsProcessed) {
        this.numberOfColorsProcessed = numberOfColorsProcessed;
    }

    public int getNumberOfColorsProcessed() {
        return numberOfColorsProcessed;
    }

    public boolean hasNewColorsForComm() {
        return newColorsForComm;
    }
}