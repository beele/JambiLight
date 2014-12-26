package be.beeles_place.jambiLight;

import be.beeles_place.jambiLight.controllers.ApplicationController;
import be.beeles_place.jambiLight.events.ShutdownEvent;
import be.beeles_place.jambiLight.utils.EventbusWrapper;
import be.beeles_place.jambiLight.utils.StageFactory;
import be.beeles_place.jambiLight.view.JambiUI.NewViewController;
import be.beeles_place.jambiLight.view.MainViewController;
import com.google.common.eventbus.EventBus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;

public class Main extends Application {

    private EventBus eventBus;
    private ApplicationController appController;

    /**
     * Bootstrap the application to start with JavaFX.
     *
     * @param args Optional application arguments.
     *             If you pass the argument "disableUI" the GUI will be disabled and only the console will be used!
     */
    public static void main(String[] args) {
        for (String arg : args) {
            if ("disableUI".equals(arg)) {
                Main m = new Main();
                m.startNoUI();
                return;
            }
        }
        //Launch the JavaFX application.
        launch(args);
    }

    /**
     * JavaFX application entry point.
     *
     * @param stage Stage object that represents the main stage.
     * @throws Exception When things go horribly wrong (the main fxml cannot be found...).
     */
    @Override
    public void start(Stage stage) throws Exception {
        eventBus = EventbusWrapper.getInstance();
        eventBus.register(this);

        //Make a new application controller.
        appController = new ApplicationController();

        //Create a new stage and load the FXML GUI.
        StageFactory.StageFactoryResult<MainViewController> result = StageFactory.getInstance().createStage("main.fxml", "JambiLight 1.0 Alpha", new Dimension(1150, 650));
        stage = result.getStage();
        //Set the stage shutdown action.
        stage.setOnCloseRequest(event -> eventBus.post(new ShutdownEvent()));

        //TODO: Use new GUI!
        StageFactory.StageFactoryResult<NewViewController> tempResult = StageFactory.getInstance().createStage("JambiUI/new.fxml", "JambiUI", new Dimension(1150, 650));
        appController.newViewController = tempResult.getController();

        //Init the application controller, giving it the stage and the view controller.
        appController.init(stage, result.getController());
    }

    /**
     * Starts the application without any user interface.
     */
    public void startNoUI() {
        //Make a new application controller.
        appController = new ApplicationController();
        //Init the application controller, giving it the stage and the view controller.
        appController.init();
    }
}