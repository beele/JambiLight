package be.beeles_place.jambiLight.utils.screenCapture;

import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.impl.*;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.lang.reflect.Constructor;

@XmlType
@XmlEnum
public enum ScreenCapperStrategy {

    @XmlEnumValue("JAVA_SCREENSHOT")
    JAVA_SCREENSHOT(ScreenCapper.class),

    @XmlEnumValue("XBMC_SCREENSHOT")
    XBMC_SCREENSHOT(XbmcScreenCapper.class),

    @XmlEnumValue("MOCK_RAINBOW")
    MOCK_RAINBOW(ScreenCapperMock.class),

    @XmlEnumValue("MOCK_JNI")
    MOCK_JNI(ScreenCapperJNI.class),

    @XmlEnumValue("DIRECT_SHOW")
    DIRECT_SHOW(DirectShowCapper.class);

    private Class captureStrategy;

    ScreenCapperStrategy(Class captureStrategy) {
        this.captureStrategy = captureStrategy;
    }

    /**
     * Returns an instance of the selected strategy on this enum.
     *
     * @return An instance of IScreenCapture as defined in the options of this enum.
     */
    public IScreenCapper getCaptureStrategy() {
        IScreenCapper temp = null;
        try {
            Class<?> clazz = Class.forName(captureStrategy.getName());
            Constructor<?> conz = clazz.getConstructor();
            temp = (IScreenCapper) conz.newInstance();
        } catch (Exception e) {
            LOGGER.getInstance().ERROR("MODE => Fatal error instantiating requested IScreenCapture logic!");
        } finally {
            return temp;
        }
    }
}