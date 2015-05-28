package be.beeles_place.jambiLight.integration;

import be.beeles_place.jambiLight.commanding.EventbusWrapper;
import be.beeles_place.jambiLight.commanding.events.application.ColorModelUpdatedEvent;
import be.beeles_place.jambiLight.communication.CommunicationStrategy;
import be.beeles_place.jambiLight.controllers.ColorController;
import be.beeles_place.jambiLight.controllers.CommunicationController;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.modes.ColorStrategy;
import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.ScreenCapperStrategy;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.logger.LoggerLevel;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.fail;

public class AmbiLightIT {

    public SettingsModel settings;
    public ColorModel model;
    public ColorController colorController;
    public CommunicationController serialCommunicator;

    public int count;

    @BeforeClass
    public static void setup() {
        LOGGER logger = LOGGER.getInstance();
        logger.setLogToFile(false);

        logger.setLogToSTDOUT(true);
        logger.setLoggerLevel(LoggerLevel.ALL);
    }

    public AmbiLightIT() {
        EventBus eventbus = EventbusWrapper.getInstance();
        eventbus.register(this);
    }

    @Test
    public void testBasicInstance() {
        settings = new SettingsModel();
        settings.setAutoConnect(true);
        settings.setInterpolated(false);
        settings.setCorrectIntensity(false);
        settings.setEnhanceColor(false);

        settings.setPixelIteratorStepSize(1);

        settings.setHorizontalMargin(0);
        settings.setVerticalMargin(0);
        settings.setHorizontalRegions(16);
        settings.setVerticalRegions(9);

        settings.setCaptureMode(ScreenCapperStrategy.MOCK_RAINBOW);
        settings.setPort("MOCK");

        model = new ColorModel();
        //Calculate the new amount of regions.
        model.setNumberOfConsolidatedRegions((settings.getHorizontalRegions() * 2) + (settings.getVerticalRegions() * 2) - 4);
        model.setScreenDimensions(new Dimension(720, 480));

        //Create and set up the communication controller.
        serialCommunicator = new CommunicationController(model, settings);
        serialCommunicator.init(CommunicationStrategy.MOCK, false);

        //Create and set up the color controller.
        colorController = new ColorController(settings, model);
        colorController.startColorStrategy(ColorStrategy.AMBILIGHT);

        count = 0;
        while(count < 1000) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail();
            }
        }
    }

    @Subscribe
    public void onColorsUpdated(ColorModelUpdatedEvent event) {
        count++;
    }
}