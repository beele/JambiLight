package be.beeles_place.model;

public class SettingsModel {

    //Below are the setting fields for the ambilight mode!
    private int horizontalRegions;
    private int verticalRegions;
    private int regionMargin;
    private int pixelIteratorStepSize;

    private boolean enhanceColor;
    private boolean ignoreCenterRegions;

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

    public int getRegionMargin() {
        return regionMargin;
    }

    public void setRegionMargin(int regionMargin) {
        this.regionMargin = regionMargin;
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

    public boolean isIgnoreCenterRegions() {
        return ignoreCenterRegions;
    }

    public void setIgnoreCenterRegions(boolean ignoreCenterRegions) {
        this.ignoreCenterRegions = ignoreCenterRegions;
    }
}
