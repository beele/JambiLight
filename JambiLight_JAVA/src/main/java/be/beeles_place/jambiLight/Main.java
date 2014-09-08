package be.beeles_place.jambiLight;

import be.beeles_place.jambiLight.controllers.ApplicationController;
import be.beeles_place.jambiLight.events.ShutdownEvent;
import be.beeles_place.jambiLight.utils.EventbusWrapper;
import com.google.common.eventbus.EventBus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    private EventBus eventBus;
    private ApplicationController appController;

    /**
     * Bootstrap the application to start with JavaFX.
     *
     * @param args Optional application arguments. Not used!
     */
    public static void main(String[] args) {
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

        //Create javafx UI.
        URL location = getClass().getResource("/be/beeles_place/jambiLight/view/main.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load(location.openStream());

        stage.setTitle("JambiLight 1.0 Alpha");
        stage.setScene(new Scene(root, 1150, 650));
        stage.show();

        //Set the stage shutdown action.
        stage.setOnCloseRequest(event -> eventBus.post(new ShutdownEvent()));

        //Init the application controller, giving it the stage and the view controller.
        appController.init(stage, fxmlLoader.getController());
    }
}