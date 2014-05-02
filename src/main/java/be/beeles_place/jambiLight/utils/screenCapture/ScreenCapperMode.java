package be.beeles_place.jambiLight.utils.screenCapture;

import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.impl.ScreenCapper;
import be.beeles_place.jambiLight.utils.screenCapture.impl.ScreenCapperJNIMock;
import be.beeles_place.jambiLight.utils.screenCapture.impl.ScreenCapperMock;
import be.beeles_place.jambiLight.utils.screenCapture.impl.XbmcScreenCapper;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.lang.reflect.Constructor;

@XmlType
@XmlEnum
public enum ScreenCapperMode {

    @XmlEnumValue("JAVA_SCREENSHOT")
    JAVA_SCREENSHOT(ScreenCapper.class),

    @XmlEnumValue("XBMC_SCREENSHOT")
    XBMC_SCREENSHOT(XbmcScreenCapper.class),

    @XmlEnumValue("MOCK_RAINBOW")
    MOCK_RAINBOW(ScreenCapperMock.class),

    @XmlEnumValue("MOCK_JNI")
    MOCK_JNI(ScreenCapperJNIMock.class);

    private Class capper;

    ScreenCapperMode(Class capperClass) {
        this.capper = capperClass;
    }

    //Getters.
    public IScreenCapper getCaptureLogic() {
        IScreenCapper temp = null;
        try {
            Class<?> clazz = Class.forName(capper.getName());
            Constructor<?> conz = clazz.getConstructor();
            temp = (IScreenCapper) conz.newInstance();
        } catch (Exception e) {
            LOGGER.getInstance().ERROR("MODE => Fatal error instantiating requested IScreenCapture mode!");
        } finally {
            return temp;
        }
    }
}