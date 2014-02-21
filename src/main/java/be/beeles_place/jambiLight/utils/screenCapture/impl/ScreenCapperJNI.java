package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;

import java.awt.*;

public class ScreenCapperJNI implements IScreenCapper {

    @Override
    public Dimension getScreenDimensions() {
        return null;
    }

    @Override
    public int[] capture() {
        return new int[0];
    }

}
