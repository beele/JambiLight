package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class TabFiveGenerateCodeEvent extends BaseEvent {

    //Content:
    public CheckBox T5_CHK_AutoConnect;
    public ComboBox<String> T5_CMB_CommChannel;
    public TextField T5_TXT_ClockPin;
    public TextField T5_TXT_DataPin;
    public ComboBox<String> T5_CMB_LedType;
}
