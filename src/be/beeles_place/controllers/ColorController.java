package be.beeles_place.controllers;

import be.beeles_place.modes.AbstractColorMode;

public class ColorController {

    private AbstractColorMode colorMode;
    private Thread colorThread;

    public ColorController() {

    }

    public String getColorMode() {
        return colorMode.getClass().getName();
    }

    public void setColorMode(AbstractColorMode newColorMode) {
        if (colorMode != null) {
            //Before setting the new color mode, stop the current one first!
            colorMode.stop();
        }

        //If no new color mode has been set.
        if (newColorMode == null) {
            return;
        } else {
            colorMode = newColorMode;
            colorThread = new Thread(colorMode);
            colorThread.setPriority(Thread.MAX_PRIORITY);
            colorThread.setName(newColorMode.getClass().getName());
            colorThread.start();
        }
    }
}
