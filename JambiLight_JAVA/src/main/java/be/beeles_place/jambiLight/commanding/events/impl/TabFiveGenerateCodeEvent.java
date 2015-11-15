package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import be.beeles_place.jambiLight.utils.LedType;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TabFiveGenerateCodeEvent extends BaseEvent {

    public TabFiveGenerateCodeEvent(Stage stage) {
        Scene scene = stage.getScene();

        T5_TXT_ClockPin = (TextField) scene.lookup("#T5_TXT_ClockPin");
        T5_TXT_DataPin = (TextField) scene.lookup("#T5_TXT_DataPin");
        T5_CMB_LedType = (ComboBox<LedType>) scene.lookup("#T5_CMB_LedType");
    }

    //Content:
    public TextField T5_TXT_ClockPin;
    public TextField T5_TXT_DataPin;
    public ComboBox<LedType> T5_CMB_LedType;
}
