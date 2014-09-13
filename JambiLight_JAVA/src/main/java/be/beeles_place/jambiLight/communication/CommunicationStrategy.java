package be.beeles_place.jambiLight.communication;

import be.beeles_place.jambiLight.communication.impl.SerialCommJSSC;
import be.beeles_place.jambiLight.communication.impl.SerialCommMock;
import be.beeles_place.jambiLight.communication.impl.SerialCommRXTX;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.lang.reflect.Constructor;

@XmlType
@XmlEnum
public enum CommunicationStrategy {

    @XmlEnumValue("COMM_MOCK")
    MOCK(SerialCommMock.class),

    @XmlEnumValue("COMM_JSSC")
    JSSC(SerialCommJSSC.class),

    @XmlEnumValue("COMM_RXTX")
    RXTX(SerialCommRXTX.class);

    private Class commStrategy;

    CommunicationStrategy(Class commStrategy) {
        this.commStrategy = commStrategy;
    }

    /**
     * Returns an instance of the selected strategy on this enum.
     *
     * @return An instance of ISerialComm as defined in the options of this enum.
     */
    public ISerialComm getCommStrategy() {
        ISerialComm temp = null;
        try {
            Class<?> clazz = Class.forName(commStrategy.getName());
            Constructor<?> conz = clazz.getConstructor();
            temp = (ISerialComm) conz.newInstance();
        } catch (Exception e) {
            LOGGER.getInstance().ERROR("COMM => Fatal error instantiating requested ISerialComm logic!");
        } finally {
            return temp;
        }
    }
}