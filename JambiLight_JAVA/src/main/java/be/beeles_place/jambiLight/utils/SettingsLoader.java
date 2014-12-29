package be.beeles_place.jambiLight.utils;

import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.ScreenCapperStrategy;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class SettingsLoader {

    private LOGGER logger;

    /**
     * Creates a new SettingsLoader instance.
     */
    public SettingsLoader() {
        logger = LOGGER.getInstance();
    }

    /**
     * Will load a settings file from disk.
     * The settings file is named settings.xml and stored in the default directory.
     * If no appropriate settings file was found, or an error occurred, new default settings will be loaded.
     *
     * @return A SettingsModel instance containing the loaded settings.
     */
    public SettingsModel loadSettingsModel() {
        logger.INFO("SETTINGS => Reading settings from disk.");

        File settingsFile = new File("settings.xml");
        SettingsModel settings = null;
        boolean createDefaultSettings = false;

        //If the settings file exists
        if(settingsFile.exists()) {
            try {
                //Unmarshal settings file to object instance.
                JAXBContext jaxbContext = JAXBContext.newInstance(SettingsModel.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                settings = (SettingsModel) jaxbUnmarshaller.unmarshal(settingsFile);
                logger.INFO("SETTINGS => Settings successfully read from disk.");
            } catch (Exception e) {
                logger.INFO("SETTINGS => Cannot read settings from disk!");
                settings = null;
                createDefaultSettings = true;
            }
        //If the settings file does not exist on disk.
        } else {
            logger.INFO("SETTINGS => No settings found on disk!");
            createDefaultSettings = true;
        }

        if(createDefaultSettings) {
            logger.INFO("SETTINGS => Creating new default settings!");
            //Create a default settings file.
            settings = new SettingsModel();

            //By default a 16/9 aspect ratio is used (perfect for fullHD)
            settings.setHorizontalRegions(16);
            settings.setVerticalRegions(11);

            //Pixel iterator is 2 by default, thus only have the screen's pixels are used.
            settings.setPixelIteratorStepSize(2);

            //No margins by default.
            settings.setHorizontalMargin(0);
            settings.setVerticalMargin(0);

            //Color enhancement is disabled by default.
            settings.setEnhanceColor(false);
            settings.setEnhanceValue(2.5f);
            settings.setEnhancePerChannel(false);
            settings.setEnhanceValueR(1f);
            settings.setEnhanceValueG(1f);
            settings.setEnhanceValueB(1f);

            //Colors should be weighed, as it gives a much nicer result.
            settings.setWeighColor(true);
            settings.setWeighFactor(2);

            //Intensity correction is enabled by default.
            settings.setCorrectIntensity(true);
            settings.setGreyDetectionThreshold(10);
            settings.setScaleUpValue(0.2f);
            settings.setScaleDownValue(0.67f);

            //Set the default screen capture method to JAVA_SCREENSHOT
            settings.setCaptureMode(ScreenCapperStrategy.JAVA_SCREENSHOT);

            //Save the new settings to disk!
            saveSettingsModel(settings);
        }

        return settings;
    }

    /**
     * Saves the given SettingsModel instance to disk.
     * The settings file is named settings.xml and stored in the default directory.
     *
     * @param settings The SettingsModel instance to save to the disk.
     */
    public void saveSettingsModel(SettingsModel settings) {
        logger.INFO("SETTINGS => Saving settings to disk.");

        File settingsFile = new File("settings.xml");

        try {
            //Unmarshal settings file to object instance.
            JAXBContext jaxbContext = JAXBContext.newInstance(SettingsModel.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(settings,settingsFile);
            logger.INFO("SETTINGS => Settings successfully saved to disk.");
        } catch (Exception e) {
            logger.INFO("SETTINGS => Cannot save settings to disk!");
        }
    }
}