package be.beeles_place.jambiLight.model;

import be.beeles_place.jambiLight.events.ColorModelUpdatedEvent;
import be.beeles_place.jambiLight.utils.EventbusWrapper;
import com.google.common.eventbus.EventBus;

public class ColorModel {

    private int[][] currentColors;
    private long actionDuration;
    private boolean colorsAreNew;

    private EventBus eventbus;
    private int numberOfConsolidatedRegions;

    public ColorModel() {
        eventbus = EventbusWrapper.getInstance();
        eventbus.register(this);
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
        colorsAreNew = false;
        return currentColors;
    }

    public boolean getNewColorsForCommAvailable() {
        return colorsAreNew;
    }

    public void setCurrentColors(int[][] currentColors) {
        this.currentColors = currentColors;
        colorsAreNew = true;
    }

    public long getActionDuration() {
        return actionDuration;
    }

    public void setActionDuration(long actionDuration) {
        this.actionDuration = actionDuration;
    }

    public void setNumberOfConsolidatedRegions(int numberOfConsolidatedRegions) {
        this.numberOfConsolidatedRegions = numberOfConsolidatedRegions;
    }

    public int getNumberOfConsolidatedRegions() {
        return numberOfConsolidatedRegions;
    }
}