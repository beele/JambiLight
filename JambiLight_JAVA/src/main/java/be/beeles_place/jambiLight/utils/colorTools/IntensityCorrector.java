package be.beeles_place.jambiLight.utils.colorTools;

import be.beeles_place.jambiLight.model.SettingsModel;

import java.awt.*;

public class IntensityCorrector {

    private int greyDetectionThreshold;
    private float scaleUpValue;
    private float scaleDownValue;

    private float[] hsb;
    private float saturation;
    private int newColor;

    /**
     * Creates a new IntensityCorrector instance.
     * @param settingsModel The SettingsModel containing the application settings.
     */
    public IntensityCorrector(SettingsModel settingsModel) {
        this.greyDetectionThreshold = settingsModel.getGreyDetectionThreshold();
        this.scaleUpValue = settingsModel.getScaleUpValue();
        this.scaleDownValue = settingsModel.getScaleDownValue();

        this.hsb = new float[3];
    }

    /**
     * Corrects the Intensity for each region.
     * White and grey values will be toned down and colors will be scaled up (increase the saturation).
     * @param regions A int[][] containing the regions (first dimension) and the Colors (second dimension R/G/B) to be corrected.
     * @return The regions with updated colors per region.
     */
    public int[][] correctIntensity(int[][] regions) {
        //Loop over each region.
        for (int[] region : regions) {
            int r = region[0];
            int g = region[1];
            int b = region[2];

            //Main goal is to detect white/grey colors and decrease their intensity.
            if(Math.abs(r - g) < greyDetectionThreshold  && Math.abs(r - b) < greyDetectionThreshold && Math.abs(g - b) < greyDetectionThreshold) {
                //0f gives black, 1f gives the same color.
                r *= scaleDownValue;
                g *= scaleDownValue;
                b *= scaleDownValue;
            } else {
                //Intensify the colors by converting them to the HSB model and increasing the saturation.
                Color.RGBtoHSB(r,g,b,hsb);
                saturation = (hsb[1] + scaleUpValue);
                //Convert the HSB color back to RGB.
                newColor = Color.HSBtoRGB(  hsb[0],
                                            saturation > 1f ? 1f : saturation,
                                            hsb[2]);

                //Split up to components of the returned int value.
                r = (newColor >> 16) & 0xFF;
                g = (newColor >> 8) & 0xFF;
                b = newColor & 0xFF;
            }

            //Assign the corrected colors back.
            region[0] = r;
            region[1] = g;
            region[2] = b;
        }

        return regions;
    }
}