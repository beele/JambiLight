package be.beeles_place.utils.colorTools;

import be.beeles_place.model.SettingsModel;

import java.awt.*;

public class IntensityCorrector {

    private int greyDetectionThreshold;
    private double scaleUpValue;
    private double scaleDownValue;

    public IntensityCorrector(SettingsModel settingsModel) {
        this.greyDetectionThreshold = settingsModel.getGreyDetectionThreshold();
        this.scaleUpValue = settingsModel.getScaleUpValue();
        this.scaleDownValue = settingsModel.getScaleDownValue();
    }

    public int[][] correctIntensity(int[][] regions) {
        //Loop over each region.
        for (int[] region : regions) {
            int r = region[0];
            int g = region[1];
            int b = region[2];

            //Main goal is to detect white/grey colors and decrease their intensity.
            if(Math.abs(r - g) < greyDetectionThreshold  && Math.abs(r - b) < greyDetectionThreshold && Math.abs(g - b) < greyDetectionThreshold) {
                r /= scaleDownValue;
                g /= scaleDownValue;
                b /= scaleDownValue;
            } else {
                //TODO: find a faster way! This is slower than a slowpoke!
                float[] hsb = new float[3];
                Color.RGBtoHSB(r,g,b,hsb);
                float newSaturation = (float)(hsb[1] + 0.2);
                newSaturation = newSaturation > 1 ? 1 : newSaturation;
                Color c = Color.getHSBColor(hsb[0],newSaturation,hsb[2]);
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
            }

            //Assign the corrected colors back.
            region[0] = r;
            region[1] = g;
            region[2] = b;
        }

        return regions;
    }
}