package be.beeles_place.jambiLight;

import be.beeles_place.jambiLight.controllers.ApplicationController;
import be.beeles_place.jambiLight.commanding.events.application.ShutdownEvent;
import be.beeles_place.jambiLight.commanding.EventbusWrapper;
import be.beeles_place.jambiLight.utils.StageFactory;
import be.beeles_place.jambiLight.view.MainViewController;
import com.google.common.eventbus.EventBus;
import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    private static boolean enableUI = true;
    private static boolean enableDebug = false;

    private EventBus eventBus;
    private ApplicationController appController;

    /**
     * Bootstrap the application to start with JavaFX.
     *
     * @param args Optional application arguments.
     *             If you pass the argument "disableUI" the GUI will be disabled and only the console will be used!
     *             If you pass the argument "enableDebug" the debug mode will enable and a performance monitor will run.
     *             Also logging level will be set to ALL and will also be output to the Standard output.
     */
    public static void main(String[] args) {
        //Run through all the given parameters and use the ones we want.
        for (String arg : args) {
            if ("disableUI".equals(arg)) {
                enableUI = false;
            }
            if("enableDebug".equals(arg)) {
                enableDebug = true;
            }
        }

        //Start with ot without UI depending on the given parameters.
        if(enableUI) {
            //Launch the JavaFX application.
            launch(args);
        } else {
            Main m = new Main();
            m.startNoUI();
        }
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

        StageFactory.StageFactoryResult<MainViewController> result = StageFactory.getInstance().createStage("mainView.fxml", "JambiLight RC1", new Dimension(1150, 650));
        //Get the stage and set the shutdown action.
        stage = result.getStage();
        stage.setOnCloseRequest(event -> eventBus.post(new ShutdownEvent()));

        //Get the controller and set the stage.
        MainViewController controller = result.getController();
        controller.setStage(stage);

        //Make a new application controller.
        //Init the application controller, giving it the stage and the view controller.
        appController = new ApplicationController(enableDebug);
        appController.init(stage, controller);
    }

    /**
     * Starts the application without any user interface.
     */
    public void startNoUI() {
        //Make a new application controller.
        //Init the application controller, giving it the stage and the view controller.
        appController = new ApplicationController(enableDebug);
        appController.init();
    }
}