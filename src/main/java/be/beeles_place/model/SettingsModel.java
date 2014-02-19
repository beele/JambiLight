package be.beeles_place.model;

public class SettingsModel {

    //JambiLight core settings.
    private int horizontalRegions;
    private int verticalRegions;

    private int horizontalMargin;
    private int verticalMargin;

    private int pixelIteratorStepSize;

    //Color enhancement settings.
    private boolean enhanceColor;

    //Region consolidation settings.
    private boolean weighColor;

    //Color correction settings.
    private boolean correctIntensity;
    private int greyDetectionThreshold;
    private double scaleUpValue;
    private double scaleDownValue;

    /**
     * Creates a new SettingsModel instance.
     * Will set all the values on their defaults.
     */
    public SettingsModel() {
        //By default a 16/9 aspect ratio is used (perfect for fullHD)
        horizontalRegions = 16;
        verticalRegions = 9;

        //No margins by default.
        horizontalMargin = 0;
        verticalMargin = 0;

        //Pixel iterator is 2 by default, thus only have the screen's pixels are used.
        pixelIteratorStepSize = 2;

        //Color enhancement is disabled by default.
        enhanceColor = false;

        //Colors should be wieghed, as it gives a much nicer result.
        weighColor = true;

        //Intensity correction is disabled by default.
        correctIntensity = false;
        greyDetectionThreshold = 10;
        scaleUpValue = 1.5;
        scaleDownValue = 1.5;
    }

    //Getters & setters.
    public int getHorizontalRegions() {
        return horizontalRegions;
    }

    public void setHorizontalRegions(int horizontalRegions) {
        this.horizontalRegions = horizontalRegions;
    }

    public int getVerticalRegions() {
        return verticalRegions;
    }

    public void setVerticalRegions(int verticalRegions) {
        this.verticalRegions = verticalRegions;
    }

    public int getHorizontalMargin() {
        return horizontalMargin;
    }

    public void setHorizontalMargin(int horizontalMargin) {
        this.horizontalMargin = horizontalMargin;
    }

    public int getVerticalMargin() {
        return verticalMargin;
    }

    public void setVerticalMargin(int verticalMargin) {
        this.verticalMargin = verticalMargin;
    }

    public int getPixelIteratorStepSize() {
        return pixelIteratorStepSize;
    }

    public void setPixelIteratorStepSize(int pixelIteratorStepSize) {
        this.pixelIteratorStepSize = pixelIteratorStepSize;
    }

    public boolean isEnhanceColor() {
        return enhanceColor;
    }

    public void setEnhanceColor(boolean enhanceColor) {
        this.enhanceColor = enhanceColor;
    }

    public boolean isCorrectIntensity() {
        return correctIntensity;
    }

    public void setCorrectIntensity(boolean correctIntensity) {
        this.correctIntensity = correctIntensity;
    }

    public boolean isWeighColor() {
        return weighColor;
    }

    public void setWeighColor(boolean weighColor) {
        this.weighColor = weighColor;
    }

    public int getGreyDetectionThreshold() {
        return greyDetectionThreshold;
    }

    public void setGreyDetectionThreshold(int greyDetectionThreshold) {
        this.greyDetectionThreshold = greyDetectionThreshold;
    }

    public double getScaleUpValue() {
        return scaleUpValue;
    }

    public void setScaleUpValue(double scaleUpValue) {
        this.scaleUpValue = scaleUpValue;
    }

    public double getScaleDownValue() {
        return scaleDownValue;
    }

    public void setScaleDownValue(double scaleDownValue) {
        this.scaleDownValue = scaleDownValue;
    }
}
