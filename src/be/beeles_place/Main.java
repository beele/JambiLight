package be.beeles_place;

import be.beeles_place.controllers.ColorController;
import be.beeles_place.model.SettingsModel;
import be.beeles_place.modes.AbstractColorMode;
import be.beeles_place.modes.impl.AmbilightMode;
import be.beeles_place.view.ScreenGridView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/be/beeles_place/view/main.fxml"));
        stage.setTitle("JambiLight 1.0 Alpha");
        stage.setScene(new Scene(root, 300, 275));
        stage.show();

        SettingsModel settings = new SettingsModel();
        settings.setEnhanceColor(false);
        settings.setHorizontalRegions(128);
        settings.setVerticalRegions(80);
        settings.setIgnoreCenterRegions(false);
        settings.setPixelIteratorStepSize(2);
        settings.setRegionMargin(2);


        //Old to be replaced swing UI.
        final ScreenGridView frame = new ScreenGridView(settings);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Show ui.
                frame.setSize(new Dimension(800,600));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

        ColorController c = new ColorController();
        AbstractColorMode mode = new AmbilightMode(settings, frame);
        c.setColorMode(mode);
    }
}
