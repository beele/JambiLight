package be.beeles_place.jambiLight;

import be.beeles_place.jambiLight.controllers.ApplicationController;
import be.beeles_place.jambiLight.events.ShutdownEvent;
import be.beeles_place.jambiLight.utils.EventbusWrapper;
import be.beeles_place.jambiLight.view.MainViewController;
import com.google.common.eventbus.EventBus;
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

    private EventBus eventBus;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        eventBus = EventbusWrapper.getInstance();
        eventBus.register(this);

        ApplicationController controller = new ApplicationController();

        //Create javafx UI.
        URL location = getClass().getResource("/be/beeles_place/jambiLight/view/main.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = (Parent) fxmlLoader.load(location.openStream());

        stage.setTitle("JambiLight 1.0 Alpha");
        stage.setScene(new Scene(root, 1150, 650));
        stage.show();

        //Set the stage shutdown action.
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                eventBus.post(new ShutdownEvent());
            }
        });
        MainViewController viewController = (MainViewController) fxmlLoader.getController();

        //Init the application controller.
        controller.init(stage, viewController);
    }
}