package be.beeles_place.jambiLight.utils.screenCapture;

import be.beeles_place.jambiLight.model.SettingsModel;

import java.awt.*;

public interface IScreenCapper {

    /**
     * Returns the primary display dimensions.
     *
     * @return A Dimension object containing the dimensions of the primary screen.
     */
    public Dimension getScreenDimensions();

    /**
     * This method should be overridden to allow for a SettingsModel object to be used inside the capture logic.
     *
     * @param settings The SettingsModel object to set.
     */
    default public void setSettings(SettingsModel settings) {
        //Do nothing!
    }

    /**
     * Captures a single frame of the primary screen.
     *
     * @return An array of int containing all the pixels that were captured.
     */
    public int[] capture();

    /**
     * This method will cleanup and dispose anything that is needed.
     */
    public void dispose();
}
