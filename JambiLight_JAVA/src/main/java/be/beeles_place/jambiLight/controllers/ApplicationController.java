package be.beeles_place.jambiLight.controllers;

import be.beeles_place.jambiLight.communication.CommunicationStrategy;
import be.beeles_place.jambiLight.events.*;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.modes.ColorStrategy;
import be.beeles_place.jambiLight.utils.EventbusWrapper;
import be.beeles_place.jambiLight.utils.SettingsLoader;
import be.beeles_place.jambiLight.utils.StageFactory;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.view.DebugViewController;
import be.beeles_place.jambiLight.view.JambiUI.NewViewController;
import be.beeles_place.jambiLight.view.MainViewController;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ApplicationController {

    private LOGGER logger;
    private EventBus eventBus;
    private SettingsLoader settingsLoader;

    //Models
    private ColorModel model;
    private SettingsModel settings;

    //Logic
    private ColorController colorController;
    private CommunicationController serialCommunicator;

    //UI vars
    private boolean enableUI = false;
    private Stage stage;
    private Stage debugStage;
    private NewViewController viewController;
    private DebugViewController debugViewController;

    //Temp & testing
    private final boolean debug = true;
    private Runtime rt;
    private int megabyteInBytes;
    private int performanceCounter;

    /**
     * Creates an ApplicationController instance.
     */
    public ApplicationController() {
        logger = LOGGER.getInstance();

        eventBus = EventbusWrapper.getInstance();
        eventBus.register(this);

        settingsLoader = new SettingsLoader();

        rt = Runtime.getRuntime();
        megabyteInBytes = 1048576;
        performanceCounter = 0;
    }

    /**
     * Initializes the ApplicationController without a GUI.
     * This will create and load settings.
     */
    public void init() {
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

        //Create settings model!
        settings = settingsLoader.loadSettingsModel();
        logger.INFO("INIT => Settings read and applied.");

        //Start the actual application core logic.
        startup();
    }

    /**
     * Initializes the ApplicationController with a GUI.
     * This will create and load settings.
     *
     * @param stage The javaFX stage object.
     * @param mainViewController The controller instance for the main view.
     */
    public void init(Stage stage, NewViewController mainViewController) {
        enableUI = true;

        init();

        this.stage = stage;
        viewController = mainViewController;

        //Set model on view controller.
        viewController.initUI(model, settings);
    }

    /**
     * Sets up everything and starts the main logic.
     */
    private void startup() {
        logger.INFO("INIT => Starting core logic and serial communication.");

        //Create color model if required!
        model = model == null ? new ColorModel() : model;
        //Calculate the new amount of regions.
        model.setNumberOfConsolidatedRegions((settings.getHorizontalRegions() * 2) + (settings.getVerticalRegions() * 2) - 4);

        //TODO: Communication strategy in UI & settings model.
        //Create and set up the communication controller.
        serialCommunicator = new CommunicationController(model, settings);
        serialCommunicator.init(CommunicationStrategy.JSSC);

        //TODO: Color strategy in UI & settings model.
        //Create and set up the color controller.
        colorController = new ColorController(settings, model);
        colorController.startColorStrategy(ColorStrategy.AMBILIGHT);
    }

    /**
     * Shuts down the main processing logic.
     */
    private void shutdown() {
        logger.INFO("INIT => Shutting down core logic and serial communication.");
        colorController.stopCurrentColorStrategy();
        serialCommunicator.close();
    }

    /**
     * Executed when the SettingsModel has been updated and the SettingsUpdatedEvent has been dispatched.
     *
     * @param event The event that was dispatched.
     */
    @Subscribe
    public void onSettingsModelUpdated(SettingsUpdatedEvent event) {
        //When the settings have been updated save them and restart the system.
        settingsLoader.saveSettingsModel(settings);
        logger.INFO("INIT => Reloading application after settings change.");
        shutdown();
        startup();
    }

    /**
     * Executed when the ColorModel has been updated and the ColorModelUpdatedEvent has been dispatched.
     *
     * @param event The event that was dispatched.
     */
    @Subscribe
    public void onColorsUpdated(ColorModelUpdatedEvent event) {
        //Update debug values.
        model.setFramerate((int)(1000 / model.getActionDuration()));
        model.setMemAvailable((int) (rt.freeMemory() / megabyteInBytes));
        model.setMemUsed((int) ((rt.totalMemory() - rt.freeMemory() )/ megabyteInBytes));
        model.setMemTotal((int) (rt.totalMemory() / megabyteInBytes));

        if(enableUI) {
            //Signal UI for update!
            Platform.runLater(() -> {
                //TODO: Maybe use a queue for the raw input view? Right now anything above 720P causes extreme lag!
                if(debugViewController != null) {
                    debugViewController.paint();
                }

                eventBus.post(new UpdateUserInterfaceEvent());
            });
        }

        /**
         * Testing only!!!!!
         *
         * Prints out the memory usage of the application.
         *
         * Make the system stops after a huge performance drop => check the logs!
         * The first performance drop should always be ignored, as it might be from connecting/starting things up.
         *
         * Disable this for release builds!
         */
        if(debug) {
            logger.DEBUG("#### HEAP USAGE ####");
            logger.DEBUG("Used mem: " + (rt.totalMemory() - rt.freeMemory() )/ megabyteInBytes);
            logger.DEBUG("Free mem: " + rt.freeMemory() / megabyteInBytes);
            logger.DEBUG("All  mem: " +  rt.totalMemory() / megabyteInBytes);
            logger.DEBUG("Max  mem: " + rt.maxMemory() / megabyteInBytes);

            if(model.getActionDuration() > 500) {
                if(performanceCounter == 0) {
                    performanceCounter++;
                } else {
                    shutdown();
                }
            }
        }
    }

    @Subscribe
    public void onVisualDebugStartRequested(VisualDebugEvent event) throws IOException {
        if(event.isStart()) {
            StageFactory.StageFactoryResult<DebugViewController> result = StageFactory.getInstance().createStage("debug.fxml", "Visual debug view => 1280 x 720", new Dimension(1280, 720));
            debugStage = result.getStage();
            debugViewController = result.getController();
            //TODO: This is not very clean!
            debugStage.setOnCloseRequest(evt -> eventBus.post(new VisualDebugEvent(false)));

            debugViewController.init(debugStage, model);
        } else {
            debugStage.close();
            debugStage = null;
            debugViewController = null;
        }
    }

    /**
     * Executed when the application shut do a full shutdown (close the app).
     * This will be executed when the ShutdownEvent has been dispatched.
     *
     * @param event The event that was dispatched.
     */
    @Subscribe
    public void onApplicationExit(ShutdownEvent event) {
        shutdown();
        logger.INFO("INIT => Application now shutting down! GOODBYE...");
        System.exit(0);
    }
}