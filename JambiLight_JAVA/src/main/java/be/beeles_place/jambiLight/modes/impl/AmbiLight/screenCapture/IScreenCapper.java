package be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture;

import be.beeles_place.jambiLight.model.SettingsModel;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;

public interface IScreenCapper {

    /**
     * Returns the primary display dimensions.
     *
     * @return A Dimension object containing the dimensions of the primary screen.
     */
    Dimension getScreenDimensions();

    /**
     * This method should be overridden to allow for a SettingsModel object to be used inside the capture logic.
     *
     * @param settings The SettingsModel object to set.
     */
    default void setSettings(SettingsModel settings) {
        //Do nothing!
    }

    /**
     * This method should return true when the implementation captures the raw bytes instead of one Integer per pixel!
     *
     * @return True if bytes are captured, false if not!
     */
    default boolean capturesBytesInsteadOfPixelInts() {
        return false;
    }

    /**
     * Captures a single frame of the primary screen.
     *
     * @return An array of int containing all the pixels that were captured. (One int per pixel)
     */
    default int[] capture() {
        throw new NotImplementedException();
    }

    /**
     * Captures a single frame of the primary screen.
     *
     * @return An array of bytes containing all the pixels that were captured. (Multiple bytes per pixel)
     */
    default byte[] captureBytes() {
        throw new NotImplementedException();
    }

    /**
     * This method will cleanup and dispose anything that is needed.
     */
    void dispose();
}
