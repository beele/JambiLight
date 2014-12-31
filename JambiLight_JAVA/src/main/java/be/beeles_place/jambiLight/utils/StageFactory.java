package be.beeles_place.jambiLight.utils;

import be.beeles_place.jambiLight.utils.logger.LOGGER;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;

public class StageFactory {

    private static StageFactory instance;

    /**
     * Protected constructor for singleton.
     */
    protected StageFactory() {
        //For singleton!
    }

    /**
     * Returns an instance of the StageFactory (singleton).
     *
     * @return An instance of the StageFactory (singleton).
     */
    public static StageFactory getInstance() {
        if(instance == null) {
            instance = new StageFactory();
        }
        return instance;
    }

    /**
     * Creates a stage and load the given FXML GUI file onto it.
     *
     * @param filename The filename of the view to load.
     * @param title The title to set on the stage.
     * @param size The dimensions to give to the stage.
     * @return A stageFactoryResult instance containing the Stage and optionally the controller (is null if there is no controller to load).
     */
    public StageFactoryResult createStage(String filename, String title, Dimension size) {
        StageFactoryResult result = null;

        try {
            URL location = StageFactory.class.getResource("/be/beeles_place/jambiLight/view/" + filename);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = fxmlLoader.load(location.openStream());

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, size.getWidth(), size.getHeight()));
            stage.show();

            result = new StageFactoryResult();
            result.setStage(stage);
            result.setController(fxmlLoader.getController());
        } catch (Exception e) {
            LOGGER.getInstance().ERROR("Cannot create stage => " + e.getMessage());
        }
        return result;
    }

    /**
     * Instance class for wrapping the results.
     */
    public class StageFactoryResult <T> {

        private Stage stage;
        private T controller;

        //Getters & setters:
        public Stage getStage() {
            return stage;
        }

        public void setStage(Stage stage) {
            this.stage = stage;
        }

        public T getController() {
            return controller;
        }

        public void setController(T controller) {
            this.controller = controller;
        }
    }
}