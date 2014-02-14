package be.beeles_place;

import be.beeles_place.controllers.ColorController;
import be.beeles_place.events.ColorModelUpdatedEvent;
import be.beeles_place.model.ColorModel;
import be.beeles_place.model.SettingsModel;
import be.beeles_place.modes.AbstractColorMode;
import be.beeles_place.modes.impl.AmbilightMode;
import be.beeles_place.utils.EventbusWrapper;
import be.beeles_place.utils.communication.Communicator;
import be.beeles_place.utils.logger.LOGGER;
import be.beeles_place.view.MainViewController;
import be.beeles_place.view.ScreenGridView;
import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

public class Main extends Application {

    private ScreenGridView view;
    private SettingsModel settings;
    private ColorModel model;
    private Communicator comm;

    private Stage stage;
    private MainViewController viewController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        EventbusWrapper.getInstance().register(this);

        //Create javafx UI.
        URL location = getClass().getResource("/be/beeles_place/view/main.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = (Parent) fxmlLoader.load(location.openStream());

        stage.setTitle("JambiLight 1.0 Alpha");
        stage.setScene(new Scene(root, 900, 563));
        stage.show();
        this.stage = stage;

        //TODO: Put this in some sort of application controller!
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

        //Create communicator!
        comm = new Communicator(model, true);
        //comm.open("mock");

        //New color controller and mode.
        ColorController c = new ColorController();
        AbstractColorMode mode = new AmbilightMode(settings, model);
        c.setColorMode(mode);

        //Set the stage shutdown action.
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //TODO: proper shutdown.
                System.exit(0);
            }
        });

        viewController = (MainViewController) fxmlLoader.getController();
        viewController.setModel(model);
    }

    @Subscribe
    public void colorsUpdated(ColorModelUpdatedEvent event) {
        LOGGER.getInstance().INFO("Pixel processing completed in : " + model.getActionDuration() + "ms");
        String title = "JambiLight => running at: " + (1000 / model.getActionDuration()) + " FPS";
        stage.setTitle(title);
        viewController.updateColors();
    }
}
