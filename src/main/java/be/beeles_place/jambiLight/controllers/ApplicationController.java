package be.beeles_place.jambiLight.controllers;

import be.beeles_place.jambiLight.events.ColorModelUpdatedEvent;
import be.beeles_place.jambiLight.events.SettingsUpdatedEvent;
import be.beeles_place.jambiLight.events.ShutdownEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.modes.AbstractColorMode;
import be.beeles_place.jambiLight.modes.impl.AmbilightMode;
import be.beeles_place.jambiLight.utils.EventbusWrapper;
import be.beeles_place.jambiLight.utils.SettingsLoader;
import be.beeles_place.jambiLight.utils.communication.CommunicationLibraries;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.impl.ScreenCapper;
import be.beeles_place.jambiLight.utils.screenCapture.impl.XbmcScreenCapper;
import be.beeles_place.jambiLight.view.MainViewController;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ApplicationController {

    private LOGGER logger;
    private EventBus eventBus;
    private SettingsLoader settingsLoader;

    //Models
    private ColorModel model;
    private SettingsModel settings;

    //Logic
    private ColorController colorController;
    private CommunicatorController serialCommunicator;

    //UI vars
    private Stage stage;
    private MainViewController viewController;

    /**
     * Creates an ApplicationController instance.
     */
    public ApplicationController() {
        logger = LOGGER.getInstance();

        eventBus = EventbusWrapper.getInstance();
        eventBus.register(this);

        settingsLoader = new SettingsLoader();
    }

    /**
     * Initializes the ApplicationController.
     * This will create and load settings.
     * @param stage The javaFX stage object.
     * @param mainViewController The controller instance for the main view.
     */
    public void init(Stage stage, MainViewController mainViewController) {
        logger.INFO("==========================================================================");
        logger.INFO("==========================================================================");
        logger.INFO("     __           __   _ __   _      __   __ ");
        logger.INFO(" __ / /__ ___ _  / /  (_) /  (_)__ _/ /  / /_");
        logger.INFO("/ // / _ `/  ' \\/ _ \\/ / /__/ / _ `/ _ \\/ __/");
        logger.INFO("\\___/\\_,_/_/_/_/_.__/_/____/_/\\_, /_//_/\\__/");
        logger.INFO("                             /___/    ");
        logger.INFO("==========================================================================");
        logger.INFO("==========================================================================");

        logger.INFO("INIT => Initializing JambiLight...");
        this.stage = stage;
        viewController = mainViewController;

        //Create settings model!
        settings = settingsLoader.loadSettingsModel();
        logger.INFO("INIT => Settings read and applied.");

        //Start the actual application core logic.
        startup();

        //Set model on view controller.
        viewController.setSettings(settings);
        viewController.setModel(model);
        viewController.initUI();
    }

    private void startup() {
        logger.INFO("INIT => Starting core logic and serial communication.");

        //Create color model if required!
        if(model == null) {
            model = new ColorModel();
        }
        //Calculate the new amount of regions.
        model.setNumberOfConsolidatedRegions(settings.getHorizontalRegions() * 2 + (settings.getVerticalRegions() * 2) - 4);

        //Create communicator!
        serialCommunicator = new CommunicatorController(model, CommunicationLibraries.JSSC);
        settings.setPorts(serialCommunicator.getPorts());
        if(settings.isAutoConnect() && settings.getPort() != null){
            serialCommunicator.open(settings.getPort());
        }

        //New color controller and mode.
        colorController = new ColorController();
        AbstractColorMode mode = new AmbilightMode(settings, model, new XbmcScreenCapper());
        colorController.setColorMode(mode);
    }

    private void shutdown() {
        logger.INFO("INIT => Shutting down core logic and serial communication.");
        colorController.stopCurrentColorMode();
        serialCommunicator.close();
    }

    @Subscribe
    public void onSettingsModelUpdated(SettingsUpdatedEvent event) {
        //When the settings have been updated save them and restart the system.
        settingsLoader.saveSettingsModel(settings);
        logger.INFO("INIT => Reloading application after settings change.");
        shutdown();
        startup();
    }

    @Subscribe
    public void onColorsUpdated(ColorModelUpdatedEvent event) {
        Platform.runLater(() -> {
            String title = "JambiLight => running at: " + (1000 / model.getActionDuration()) + " FPS";
            stage.setTitle(title);
            viewController.updateColors();
        });
    }

    @Subscribe
    public void onApplicationExit(ShutdownEvent event) {
        shutdown();
        logger.INFO("INIT => Application now shutting down! GOODBYE...");
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