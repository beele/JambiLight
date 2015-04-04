package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TabFourUpdateEvent extends BaseEvent {

    public TabFourUpdateEvent(Stage stage) {
        Scene scene = stage.getScene();

        T4_CHK_EnhanceColors = (CheckBox) scene.lookup("#T4_CHK_EnhanceColors");
        T4_TXT_EnhancementValue = (TextField) scene.lookup("#T4_TXT_EnhancementValue");
        T4_CHK_EnhancePerChannel = (CheckBox) scene.lookup("#T4_CHK_EnhancePerChannel");
        T4_TXT_ChannelRed = (TextField) scene.lookup("#T4_TXT_ChannelRed");
        T4_TXT_ChannelGreen = (TextField) scene.lookup("#T4_TXT_ChannelGreen");
        T4_TXT_ChannelBlue = (TextField) scene.lookup("#T4_TXT_ChannelBlue");
        T4_CHK_CorrectIntensity = (CheckBox) scene.lookup("#T4_CHK_CorrectIntensity");
        T4_TXT_GreyThreshold = (TextField) scene.lookup("#T4_TXT_GreyThreshold");
        T4_TXT_ScaleUp = (TextField) scene.lookup("#T4_TXT_ScaleUp");
        T4_TXT_ScaleDown = (TextField) scene.lookup("#T4_TXT_ScaleDown");
    }

    //Content:
    public CheckBox T4_CHK_EnhanceColors;
    public TextField T4_TXT_EnhancementValue;
    public CheckBox T4_CHK_EnhancePerChannel;
    public TextField T4_TXT_ChannelRed;
    public TextField T4_TXT_ChannelGreen;
    public TextField T4_TXT_ChannelBlue;
    public CheckBox T4_CHK_CorrectIntensity;
    public TextField T4_TXT_GreyThreshold;
    public TextField T4_TXT_ScaleUp;
    public TextField T4_TXT_ScaleDown;
}