package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.ScreenCapperStrategy;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class TabTwoUpdateEvent extends BaseEvent {

    public TabTwoUpdateEvent(Stage stage) {
        Scene scene = stage.getScene();

        T2_CMB_CaptureMode = (ComboBox<ScreenCapperStrategy>) scene.lookup("#T2_CMB_CaptureMode");
        T2_CMB_DirectShowDevices = (ComboBox<String>) scene.lookup("#T2_CMB_DirectShowDevices");
    }

    //Content:
    public ComboBox<ScreenCapperStrategy> T2_CMB_CaptureMode;
    public ComboBox<String> T2_CMB_DirectShowDevices;
}