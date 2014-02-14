package be.beeles_place.controllers;

import be.beeles_place.events.ColorModelUpdatedEvent;
import be.beeles_place.events.ShutdownEvent;
import be.beeles_place.model.ColorModel;
import be.beeles_place.model.SettingsModel;
import be.beeles_place.modes.AbstractColorMode;
import be.beeles_place.modes.impl.AmbilightMode;
import be.beeles_place.utils.EventbusWrapper;
import be.beeles_place.utils.communication.Communicator;
import be.beeles_place.utils.logger.LOGGER;
import be.beeles_place.view.MainViewController;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.stage.Stage;

public class ApplicationController {

    private EventBus eventBus;

    private ColorModel model;
    private SettingsModel settings;
    private Communicator comm;

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
        settings.setEnhanceColor(false);
        //TODO: for now keep horizontal and vertical at these values or the UI will break!
        settings.setHorizontalRegions(16);
        settings.setVerticalRegions(10);
        settings.setPixelIteratorStepSize(2);
        settings.setRegionMargin(2);

        //Create color model!
        model = new ColorModel();

        //Set model on view controller.
        viewController.setModel(model);

        //Create communicator!
        comm = new Communicator(model, true);
        //comm.open("mock");

        //New color controller and mode.
        ColorController c = new ColorController();
        AbstractColorMode mode = new AmbilightMode(settings, model);
        c.setColorMode(mode);
    }

    @Subscribe
    public void colorsUpdated(ColorModelUpdatedEvent event) {
        LOGGER.getInstance().INFO("Pixel processing completed in : " + model.getActionDuration() + "ms");
        String title = "JambiLight => running at: " + (1000 / model.getActionDuration()) + " FPS";
        stage.setTitle(title);
        viewController.updateColors();
    }

    @Subscribe
    public void shutdown(ShutdownEvent event) {
        //TODO: shut down completely!
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