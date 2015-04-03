package be.beeles_place.jambiLight.model;

import be.beeles_place.jambiLight.commanding.events.ColorModelUpdatedEvent;
import be.beeles_place.jambiLight.commanding.EventbusWrapper;
import com.google.common.eventbus.EventBus;

import java.awt.*;

/**
 * This class contains data for the application.
 */
public class ColorModel {

    private Dimension screenDimensions;
    private int memUsed;
    private int memAvailable;
    private int memTotal;
    private int framerate;

    private int[] rawImageData;
    private int rawWidth;
    private int rawHeight;

    private int[][] currentColors;
    private int[][] previousColors;
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
    public Dimension getScreenDimensions() {
        return screenDimensions;
    }

    public void setScreenDimensions(Dimension screenDimensions) {
        this.screenDimensions = screenDimensions;
    }

    public int getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(int memUsed) {
        this.memUsed = memUsed;
    }

    public int getMemAvailable() {
        return memAvailable;
    }

    public void setMemAvailable(int memAvailable) {
        this.memAvailable = memAvailable;
    }

    public int getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(int memTotal) {
        this.memTotal = memTotal;
    }

    public int getFramerate() {
        return framerate;
    }

    public void setFramerate(int framerate) {
        this.framerate = framerate;
    }

    public int[] getRawImageData() {
        return rawImageData;
    }

    public void setRawImageData(int[] rawImageData) {
        this.rawImageData = rawImageData;
    }

    public int getRawWidth() {
        return rawWidth;
    }

    public void setRawWidth(int rawWidth) {
        this.rawWidth = rawWidth;
    }

    public int getRawHeight() {
        return rawHeight;
    }

    public void setRawHeight(int rawHeight) {
        this.rawHeight = rawHeight;
    }

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

    public int[][] getPreviousColors() {
        return previousColors;
    }

    public void setPreviousColors(int[][] previousColors) {
        this.previousColors = previousColors;
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