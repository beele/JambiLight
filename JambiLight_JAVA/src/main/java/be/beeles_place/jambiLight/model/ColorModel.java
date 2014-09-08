package be.beeles_place.jambiLight.model;

import be.beeles_place.jambiLight.events.ColorModelUpdatedEvent;
import be.beeles_place.jambiLight.utils.EventbusWrapper;
import com.google.common.eventbus.EventBus;

/**
 * This class contains data for the application.
 */
public class ColorModel {

    private int[][] currentColors;
    private long actionDuration;
    private boolean colorsAreNew;

    private EventBus eventbus;
    private int numberOfConsolidatedRegions;

    /**
     * Constructor.
     */
    public ColorModel() {
        eventbus = EventbusWrapper.getInstance();
        eventbus.register(this);
    }

    /**
     * Published an event over the EventBus indicating the model has been updated.
     * The event dispatched is of the ColorModelUpdatedEvent.
     * Any interested parties can listen for this event on the EventBus.
     */
    public void publishModelUpdate() {
        eventbus.post(new ColorModelUpdatedEvent());
    }

    //Getters & setters.
    /**
     * Returns the colors on the model.
     * Colors are formatted as int[][] where the outer array is the number of consolidated pixels and the inner
     * array are the individual color components [R/G/B].
     *
     * @return The two dimensional array of colors.
     */
    public int[][] getCurrentColors() {
        return currentColors;
    }

    /**
     * Returns the colors on the model.
     * Colors are formatted as int[][] where the outer array is the number of consolidated pixels and the inner
     * array are the individual color components [R/G/B].
     *
     * @return The two dimensional array of colors.
     */
    public int[][]getCurrentColorsForComm() {
        colorsAreNew = false;
        return currentColors;
    }

    /**
     * Returns true if new colors have been set on the model after they were last read by getCurrentColorsForComm().
     *
     * @return True if the colors are new, false if not.
     */
    public boolean areNewColorsForCommAvailable() {
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