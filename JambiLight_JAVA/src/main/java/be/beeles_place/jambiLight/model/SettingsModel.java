package be.beeles_place.jambiLight.model;

import be.beeles_place.jambiLight.utils.screenCapture.ScreenCapperStrategy;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 * This class contains the settings for the application.
 */
@XmlRootElement(name = "settings")
public class SettingsModel {

    //JambiLight core settings.
    private int horizontalRegions;                  //Between 4 and MAX_INT (please lower than 100).
    private int verticalRegions;                    //Between 4 and MAX_INT (please lower than 100).

    private int horizontalMargin;                   //Between 0 and horizontalRegions - 2.
    private int verticalMargin;                     //Between 0 and verticalRegions - 2.

    private int pixelIteratorStepSize;              //Between 1 and 10 (please lower than 5).

    //Color enhancement settings.
    private boolean enhanceColor;                   //True or false.
    private float enhanceValue;                     //Between 1 and MAX_FLOAT (please lower than 10).

    //Region consolidation settings.
    private boolean weighColor;                     //True or false
    private int weighFactor;                        //Between 1 and 5.

    //Color correction settings.
    private boolean correctIntensity;               //True or false.
    private int greyDetectionThreshold;             //Between 0 and 255 (please lower than 20).
    private float scaleUpValue;                     //Between 0.0f and 1.0f.
    private float scaleDownValue;                   //Between 0.0f and 1.0f.

    //Comm port settings.
    private String port;                            //String that contains the name of the port e.g: COM3.
    private List<String> ports;                     //A list of String objects containing all available comm ports.
    private boolean autoConnect;                    //True or false.

    private ScreenCapperStrategy captureMode;       //Enum value of type ScreenCapperMode, containing the active capture strategy.

    /**
     * Constructor.
     */
    public SettingsModel() {

    }

    //Getters & setters.
    public int getHorizontalRegions() {
        return horizontalRegions;
    }

    @XmlElement
    public void setHorizontalRegions(int horizontalRegions) {
        this.horizontalRegions = horizontalRegions;
    }

    public int getVerticalRegions() {
        return verticalRegions;
    }

    @XmlElement
    public void setVerticalRegions(int verticalRegions) {
        this.verticalRegions = verticalRegions;
    }

    public int getHorizontalMargin() {
        return horizontalMargin;
    }

    @XmlElement
    public void setHorizontalMargin(int horizontalMargin) {
        this.horizontalMargin = horizontalMargin;
    }

    public int getVerticalMargin() {
        return verticalMargin;
    }

    @XmlElement
    public void setVerticalMargin(int verticalMargin) {
        this.verticalMargin = verticalMargin;
    }

    public int getPixelIteratorStepSize() {
        return pixelIteratorStepSize;
    }

    @XmlElement
    public void setPixelIteratorStepSize(int pixelIteratorStepSize) {
        this.pixelIteratorStepSize = pixelIteratorStepSize;
    }

    public boolean isEnhanceColor() {
        return enhanceColor;
    }

    @XmlElement
    public void setEnhanceColor(boolean enhanceColor) {
        this.enhanceColor = enhanceColor;
    }

    public float getEnhanceValue() {
        return enhanceValue;
    }

    @XmlElement
    public void setEnhanceValue(float enhanceValue) {
        this.enhanceValue = enhanceValue;
    }

    public boolean isCorrectIntensity() {
        return correctIntensity;
    }

    @XmlElement
    public void setCorrectIntensity(boolean correctIntensity) {
        this.correctIntensity = correctIntensity;
    }

    public boolean isWeighColor() {
        return weighColor;
    }

    @XmlElement
    public void setWeighColor(boolean weighColor) {
        this.weighColor = weighColor;
    }

    public int getWeighFactor() {
        return weighFactor;
    }

    @XmlElement
    public void setWeighFactor(int weighFactor) {
        this.weighFactor = weighFactor;
    }

    public int getGreyDetectionThreshold() {
        return greyDetectionThreshold;
    }

    @XmlElement
    public void setGreyDetectionThreshold(int greyDetectionThreshold) {
        this.greyDetectionThreshold = greyDetectionThreshold;
    }

    public float getScaleUpValue() {
        return scaleUpValue;
    }

    @XmlElement
    public void setScaleUpValue(float scaleUpValue) {
        this.scaleUpValue = scaleUpValue;
    }

    public float getScaleDownValue() {
        return scaleDownValue;
    }

    @XmlElement
    public void setScaleDownValue(float scaleDownValue) {
        this.scaleDownValue = scaleDownValue;
    }

    public String getPort() {
        return port;
    }

    @XmlElement
    public void setPort(String port) {
        this.port = port;
    }

    public boolean isAutoConnect() {
        return autoConnect;
    }

    @XmlElement
    public void setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }

    public ScreenCapperStrategy getCaptureMode() {
        return captureMode;
    }

    @XmlElement
    public void setCaptureMode(ScreenCapperStrategy captureMode) {
        this.captureMode = captureMode;
    }

    //Getters & setters for fields not in xml
    public List<String> getPorts() {
        return ports;
    }

    @XmlTransient
    public void setPorts(List<String> ports) {
        this.ports = ports;
    }
}