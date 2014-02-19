package be.beeles_place.controllers;

import be.beeles_place.events.ColorModelUpdatedEvent;
import be.beeles_place.events.ShowPreferencesEvent;
import be.beeles_place.events.ShutdownEvent;
import be.beeles_place.model.ColorModel;
import be.beeles_place.model.SettingsModel;
import be.beeles_place.modes.AbstractColorMode;
import be.beeles_place.modes.impl.AmbilightMode;
import be.beeles_place.utils.EventbusWrapper;
import be.beeles_place.utils.communication.CommunicationLibraries;
import be.beeles_place.utils.communication.Communicator;
import be.beeles_place.utils.logger.LOGGER;
import be.beeles_place.view.MainViewController;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.stage.Stage;

public class ApplicationController {

    private EventBus eventBus;

    //Models
    private ColorModel model;
    private SettingsModel settings;

    //Logic
    private ColorController colorController;
    private Communicator serialCommunicator;

    //UI vars
    private Stage stage;
    private MainViewController viewController;

    /**
     * Creates an ApplicationController instance.
     */
    public ApplicationController() {
        eventBus = EventbusWrapper.getInstance();
        eventBus.register(this);
    }

    public void init(Stage stage, MainViewController mainViewController) {
        this.stage = stage;
        viewController = mainViewController;

        //Create settings model!
        settings = new SettingsModel();
        settings.setHorizontalRegions(20);
        settings.setVerticalRegions(14);
        settings.setPixelIteratorStepSize(2);
        settings.setHorizontalMargin(0);
        settings.setVerticalMargin(0);
        settings.setEnhanceColor(false);
        settings.setCorrectIntensity(false);

        //Create color model!
        model = new ColorModel();
        model.setNumberOfColorsProcessed(settings.getHorizontalRegions() * 2 + (settings.getVerticalRegions() * 2) - 4);

        //Set model on view controller.
        viewController.setSettings(settings);
        viewController.setModel(model);
        viewController.initUI();

        //Create communicator!
        serialCommunicator = new Communicator(model, CommunicationLibraries.JSSC);
        for (String s : serialCommunicator.getPorts()) {
            System.out.println(s);
        }
        //TODO: improve serial communicator. (mock implementation)
        //TODO: get this port from the UI.
        //serialCommunicator.open("COM4");

        //New color controller and mode.
        colorController = new ColorController();
        AbstractColorMode mode = new AmbilightMode(settings, model);
        colorController.setColorMode(mode);
    }

    @Subscribe
    public void onColorsUpdated(ColorModelUpdatedEvent event) {
        LOGGER.getInstance().INFO("Pixel processing completed in : " + model.getActionDuration() + "ms");
        String title = "JambiLight => running at: " + (1000 / model.getActionDuration()) + " FPS";
        stage.setTitle(title);
        viewController.updateColors();
    }

    @Subscribe
    public void ShowPreferencesEvent(ShowPreferencesEvent event) {
        //TODO: show preferences window!
    }

    @Subscribe
    public void onShutdown(ShutdownEvent event) {
        colorController.stopCurrentColorMode();
        serialCommunicator.close();
        System.exit(0);
    }

    //Getters & setters.
    public MainViewController getViewController() {
        return viewController;
    }

    public void setViewController(MainViewController viewController) {
        this.viewController = viewController;
    }
}