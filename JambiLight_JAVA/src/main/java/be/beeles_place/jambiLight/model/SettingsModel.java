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
    private float enhanceValue;                     //Between 1f and MAX_FLOAT (please lower than 10.0f).
    private boolean enhancePerChannel;              //True or false.
    private float enhanceValueR;                    //Between 0.0f and MAX_FLOAT (please lower than 10.0f).
    private float enhanceValueG;                    //Between 0.0f and MAX_FLOAT (please lower than 10.0f).
    private float enhanceValueB;                    //Between 0.0f and MAX_FLOAT (please lower than 10.0f).

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
    private String directShowDeviceName;            //String that contains the device name used for DirectShow capture.

    /**
     * Constructor.
     */
    public SettingsModel() {

    }

    //Getters & setters.
    /**
     * The number of horizontal regions.
     * @return The number of horizontal regions.
     */
    public int getHorizontalRegions() {
        return horizontalRegions;
    }

    @XmlElement
    public void setHorizontalRegions(int horizontalRegions) {
        this.horizontalRegions = horizontalRegions;
    }

    /**
     * The number of vertical regions.
     * @return The number of vertical regions.
     */
    public int getVerticalRegions() {
        return verticalRegions;
    }

    @XmlElement
    public void setVerticalRegions(int verticalRegions) {
        this.verticalRegions = verticalRegions;
    }

    /**
     * An int representing the horizontal margin.
     * @return An int representing the horizontal margin.
     */
    public int getHorizontalMargin() {
        return horizontalMargin;
    }

    @XmlElement
    public void setHorizontalMargin(int horizontalMargin) {
        this.horizontalMargin = horizontalMargin;
    }

    /**
     * An int representing the vertical margin.
     * @return An int representing the vertical margin.
     */
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

    public boolean isEnhancePerChannel() {
        return enhancePerChannel;
    }

    @XmlElement
    public void setEnhancePerChannel(boolean enhancePerChannel) {
        this.enhancePerChannel = enhancePerChannel;
    }

    public float getEnhanceValueR() {
        return enhanceValueR;
    }

    @XmlElement
    public void setEnhanceValueR(float enhanceValueR) {
        this.enhanceValueR = enhanceValueR;
    }

    public float getEnhanceValueG() {
        return enhanceValueG;
    }

    @XmlElement
    public void setEnhanceValueG(float enhanceValueG) {
        this.enhanceValueG = enhanceValueG;
    }

    public float getEnhanceValueB() {
        return enhanceValueB;
    }

    @XmlElement
    public void setEnhanceValueB(float enhanceValueB) {
        this.enhanceValueB = enhanceValueB;
    }

    public boolean isCorrectIntensity() {
        return correctIntensity;
    }

    @XmlElement
    public void setCorrectIntensity(boolean correctIntensity) {
        this.correctIntensity = correctIntensity;
    }

    /**
     * A boolean indicating if the colors need to be weighed or not.
     * @return A boolean indicating if the colors need to be weighed or not.
     */
    public boolean isWeighColor() {
        return weighColor;
    }

    @XmlElement
    public void setWeighColor(boolean weighColor) {
        this.weighColor = weighColor;
    }

    /**
     * The factor which the weight is calculated with. A higher number will result in bigger weight steps.
     * @return The factor which the weight is calculated with. A higher number will result in bigger weight steps.
     */
    public int getWeighFactor() {
        return weighFactor;
    }

    @XmlElement
    public void setWeighFactor(int weighFactor) {
        this.weighFactor = weighFactor;
    }

    /**
     * The value that is used to detect white/grey/black colors. (R=G=B +/- threshold)
     * @return The value that is used to detect white/grey/black colors. (R=G=B +/- threshold)
     */
    public int getGreyDetectionThreshold() {
        return greyDetectionThreshold;
    }

    @XmlElement
    public void setGreyDetectionThreshold(int greyDetectionThreshold) {
        this.greyDetectionThreshold = greyDetectionThreshold;
    }

    /**
     * The value that is used to increase the intensity.
     * @return The value that is used to increase the intensity.
     */
    public float getScaleUpValue() {
        return scaleUpValue;
    }

    @XmlElement
    public void setScaleUpValue(float scaleUpValue) {
        this.scaleUpValue = scaleUpValue;
    }

    /**
     * The value that is used to decrease the intensity.
     * @return The value that is used to decrease the intensity.
     */
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

    public String getDirectShowDeviceName() {
        return directShowDeviceName;
    }

    @XmlElement
    public void setDirectShowDeviceName(String directShowDeviceName) {
        this.directShowDeviceName = directShowDeviceName;
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