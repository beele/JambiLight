package be.beeles_place.jambiLight.model;

import java.util.List;

public class SettingsModel {

    //JambiLight core settings.
    private int horizontalRegions;              //Between 4 and MAX_INT (please lower than 100)
    private int verticalRegions;                //Between 4 and MAX_INT (please lower than 100)

    private int horizontalMargin;               //Between 0 and horizontalRegions - 2
    private int verticalMargin;                 //Between 0 and verticalRegions - 2

    private int pixelIteratorStepSize;          //Between 1 and 10 (please lower than 5)

    //Color enhancement settings.
    private boolean enhanceColor;               //True or false

    //Region consolidation settings.
    private boolean weighColor;                 //True or false

    //Color correction settings.
    private boolean correctIntensity;           //True or false
    private int greyDetectionThreshold;         //Between 0 and 255 (please lower than 20)
    private float scaleUpValue;                 //Between 0.0f and 1.0f
    private float scaleDownValue;               //Between 0.0f and 1.0f

    //Comm port settings.
    private String port;                        //String that contains the name of the port e.g: COM3
    private List<String> ports;                 //A list of String objects containing all available comm ports.
    private boolean autoConnect;                //True or false.

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

        //Colors should be weighed, as it gives a much nicer result.
        weighColor = true;

        //Intensity correction is disabled by default.
        correctIntensity = false;
        greyDetectionThreshold = 10;
        scaleUpValue = 0.2f;
        scaleDownValue = 0.67f;
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

    public float getScaleUpValue() {
        return scaleUpValue;
    }

    public void setScaleUpValue(float scaleUpValue) {
        this.scaleUpValue = scaleUpValue;
    }

    public float getScaleDownValue() {
        return scaleDownValue;
    }

    public void setScaleDownValue(float scaleDownValue) {
        this.scaleDownValue = scaleDownValue;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<String> getPorts() {
        return ports;
    }

    public void setPorts(List<String> ports) {
        this.ports = ports;
    }

    public boolean isAutoConnect() {
        return autoConnect;
    }

    public void setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }
}
