package be.beeles_place.jambiLight.utils.screenCapture;

import be.beeles_place.jambiLight.utils.logger.LOGGER;
import org.bytedeco.javacpp.videoInputLib;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class with static methods to help with device enumeration for DirectShow from JavaCV.
 */
public class DirectShowEnumerator {

    /**
     * Enumerates all the connected DirectShow (or OSX equivalent) devices in a map.
     * The key is the device id, the value the name of the device.
     *
     * @return A map with the enumerated devices, an empty map if an error occurs.
     */
    public static Map<Integer, String> enumerateDirectShowDevices() {
        LOGGER logger = LOGGER.getInstance();
        try {
            logger.DEBUG("DirectShowEnumerator ==> Enumerating devices:");

            Map<Integer, String> devices = new HashMap<>();

            int deviceCount = videoInputLib.videoInput.listDevices();
            for (int i = 0; i < deviceCount; i++) {
                String name = videoInputLib.videoInput.getDeviceName(i).getString();
                logger.DEBUG("DirectShowEnumerator ==> Input device: " + name + ", device index is " + i);

                devices.put(i, name);
            }

            return devices;
        } catch (Exception e) {
            logger.ERROR("DirectShowEnumerator ==> Fatal error in JAVACV: " + e.getMessage());
            return new HashMap<>();
        } catch (Error e) {
            logger.ERROR("DirectShowEnumerator ==> Fatal error in JAVACV: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Retrieves the id of the device for a given name.
     * If the name is null or empty -1 is returned.
     * If no match is found, -1 is returned.
     * If multiple matches are available, the first one is returned.
     *
     * @param name Name of the device to find the id for.
     * @return The id of the device, -1 if input is faulty, no match was found or an error occurred!
     */
    public static int findDeviceIdForName(String name) {
        LOGGER logger = LOGGER.getInstance();
        try {
            if(name == null || name.trim().isEmpty()) {
                return -1;
            }

            Map<Integer, String> directShowDevices = enumerateDirectShowDevices();
            for (Map.Entry<Integer, String> entry : directShowDevices.entrySet()) {
                if(name.equals(entry.getValue())) {
                    logger.DEBUG("DirectShowEnumerator => Match found => Device: " + entry.getValue() + " id: " + entry.getKey());
                    return entry.getKey();
                }
            }

            //If no match was found!
            return -1;
        } catch (Exception e) {
            logger.ERROR("DirectShowEnumerator ==> Fatal error in JAVACV: " + e.getMessage());
            return -1;
        } catch (Error e) {
            logger.ERROR("DirectShowEnumerator ==> Fatal error in JAVACV: " + e.getMessage());
            return -1;
        }
    }
}