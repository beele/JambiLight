package be.beeles_place.jambiLight.modes;

import be.beeles_place.jambiLight.modes.impl.AmbiLight.AmbilightStrategy;
import be.beeles_place.jambiLight.utils.logger.LOGGER;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.lang.reflect.Constructor;

@XmlType
@XmlEnum
public enum ColorStrategy {

    @XmlEnumValue("MODE_AMBILIGHT")
    AMBILIGHT(AmbilightStrategy.class);

    private Class colorStrategy;

    ColorStrategy(Class colorStrategy) {
        this.colorStrategy = colorStrategy;
    }

    /**
     * Returns an instance of the selected strategy on this enum.
     *
     * @return An instance of IColorMode as defined in the options of this enum.
     */
    public IColorMode getColorStrategy() {
        IColorMode temp = null;
        try {
            Class<?> clazz = Class.forName(colorStrategy.getName());
            Constructor<?> conz = clazz.getConstructor();
            temp = (IColorMode) conz.newInstance();
        } catch (Exception e) {
            LOGGER.getInstance().ERROR("MODE => Fatal error instantiating requested IColorMode logic!");
        } finally {
            return temp;
        }
    }
}
