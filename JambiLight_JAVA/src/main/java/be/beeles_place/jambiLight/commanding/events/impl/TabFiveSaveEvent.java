package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TabFiveSaveEvent extends BaseEvent {

    public TabFiveSaveEvent(Stage stage) {
        Scene scene = stage.getScene();

        T5_CHK_AutoConnect = (CheckBox) scene.lookup("#T5_CHK_AutoConnect");
        T5_CMB_CommChannel = (ComboBox<String>) scene.lookup("#T5_CMB_CommChannel");
    }

    //Content:
    public CheckBox T5_CHK_AutoConnect;
    public ComboBox<String> T5_CMB_CommChannel;
}