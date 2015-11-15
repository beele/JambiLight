package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import be.beeles_place.jambiLight.utils.LedType;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TabFiveUpdateEvent extends BaseEvent {

    public TabFiveUpdateEvent(Stage stage) {
        Scene scene = stage.getScene();

        T5_CHK_AutoConnect = (CheckBox) scene.lookup("#T5_CHK_AutoConnect");
        T5_CMB_CommChannel = (ComboBox<String>) scene.lookup("#T5_CMB_CommChannel");
        T5_TXT_ClockPin = (TextField) scene.lookup("#T5_TXT_ClockPin");
        T5_TXT_DataPin = (TextField) scene.lookup("#T5_TXT_DataPin");
        T5_CMB_LedType = (ComboBox<LedType>) scene.lookup("#T5_CMB_LedType");
    }

    //Content:
    public CheckBox T5_CHK_AutoConnect;
    public ComboBox<String> T5_CMB_CommChannel;
    public TextField T5_TXT_ClockPin;
    public TextField T5_TXT_DataPin;
    public ComboBox<LedType> T5_CMB_LedType;
}
