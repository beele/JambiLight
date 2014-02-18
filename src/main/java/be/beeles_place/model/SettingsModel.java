package be.beeles_place.model;

public class SettingsModel {

    //Below are the setting fields for the ambilight mode!
    private int horizontalRegions;
    private int verticalRegions;

    private int horizontalMargin;
    private int verticalMargin;

    private int pixelIteratorStepSize;

    private boolean enhanceColor;

    /**
     * Creates a new SettingsModel instance.
     * Will set all the values on their defaults.
     */
    public SettingsModel() {
        horizontalRegions = 16;
        verticalRegions = 9;

        horizontalMargin = 0;
        verticalMargin = 0;

        pixelIteratorStepSize = 2;
        enhanceColor = false;
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
}
