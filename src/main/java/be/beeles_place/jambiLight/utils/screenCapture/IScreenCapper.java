package be.beeles_place.jambiLight.utils.screenCapture;

import java.awt.*;

public interface IScreenCapper {

    /**
     * Returns the primary display dimensions.
     * @return A Dimension object containing the dimensions of the primary screen.
     */
    Dimension getScreenDimensions();

    /**
     * Captures a single frame of the primary screen.
     * @return An array of int containing all the pixels that were captured.
     */
    int[] capture();

    /**
     * This method will cleanup and dispose anything that is needed.
     */
    void dispose();
}
