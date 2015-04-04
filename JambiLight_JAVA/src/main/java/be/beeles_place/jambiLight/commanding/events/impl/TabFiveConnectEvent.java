package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TabFiveConnectEvent extends BaseEvent {

    public TabFiveConnectEvent(Stage stage) {
        Scene scene = stage.getScene();

        T5_CMB_CommChannel = (ComboBox<String>) scene.lookup("#T5_CMB_CommChannel");
    }

    //Content:
    public ComboBox<String> T5_CMB_CommChannel;
}