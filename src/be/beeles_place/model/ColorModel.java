package be.beeles_place.model;

import be.beeles_place.events.ColorModelUpdatedEvent;
import be.beeles_place.utils.EventbusWrapper;
import com.google.common.eventbus.EventBus;

import java.awt.*;

public class ColorModel {

    private int[] currentColor;
    private int[][] currentColors;
    private long actionDuration;

    private EventBus eventbus;

    public ColorModel() {
        eventbus = EventbusWrapper.getInstance();
        eventbus.register(this);

        currentColor = new int[]{0,255,0};
    }

    //Getters & setters.
    public Color getCurrentColor() {
        return new Color(currentColor[0],currentColor[1],currentColor[2]);
    }

    public void setCurrentColor(Color color) {
        currentColor = new int[]{color.getRed(), color.getGreen(), color.getBlue()};
    }

    public int[][] getCurrentColors() {
        return currentColors;
    }

    public void setCurrentColors(int[][] currentColors) {
        this.currentColors = currentColors;
        //Notify about the updated colors!
        eventbus.post(new ColorModelUpdatedEvent());
    }

    public long getActionDuration() {
        return actionDuration;
    }

    public void setActionDuration(long actionDuration) {
        this.actionDuration = actionDuration;
    }
}