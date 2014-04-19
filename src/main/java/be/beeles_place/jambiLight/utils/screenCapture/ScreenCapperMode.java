package be.beeles_place.jambiLight.utils.screenCapture;

import be.beeles_place.jambiLight.utils.screenCapture.impl.ScreenCapper;
import be.beeles_place.jambiLight.utils.screenCapture.impl.ScreenCapperJNIMock;
import be.beeles_place.jambiLight.utils.screenCapture.impl.ScreenCapperMock;
import be.beeles_place.jambiLight.utils.screenCapture.impl.XbmcScreenCapper;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum
public enum ScreenCapperMode {

    @XmlEnumValue("JAVA_SCREENSHOT")
    JAVA_SCREENSHOT(new ScreenCapper()),

    @XmlEnumValue("XBMC_SCREENSHOT")
    XBMC_SCREENSHOT(new XbmcScreenCapper()),

    @XmlEnumValue("MOCK_RAINBOW")
    MOCK_RAINBOW(new ScreenCapperMock(20)),

    @XmlEnumValue("MOCK_JNI")
    MOCK_JNI(new ScreenCapperJNIMock());

    private IScreenCapper capper;

    ScreenCapperMode(IScreenCapper capper) {
        this.capper = capper;
    }

    //Getters.
    public IScreenCapper getCaptureLogic() {
        return capper;
    }
}