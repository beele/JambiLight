package be.beeles_place.utils.colorTools;

import be.beeles_place.model.SettingsModel;

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
                //Increase intensity of colors.
                r *= scaleUpValue;
                g *= scaleUpValue;
                b *= scaleUpValue;

                //Safety check!
                r = r < 256 ? r : 255;
                g = g < 256 ? g : 255;
                b = b < 256 ? b : 255;
            }

            //Assign the corrected colors back.
            region[0] = r;
            region[1] = g;
            region[2] = b;
        }

        return regions;
    }
}