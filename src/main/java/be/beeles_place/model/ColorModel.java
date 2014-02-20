package be.beeles_place.model;

import be.beeles_place.events.ColorModelUpdatedEvent;
import be.beeles_place.utils.EventbusWrapper;
import com.google.common.eventbus.EventBus;

public class ColorModel {

    private int[][] currentColors;
    private long actionDuration;

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
        return currentColors;
    }

    public void setCurrentColors(int[][] currentColors) {
        this.currentColors = currentColors;
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