package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class TabFourSaveEvent extends BaseEvent {

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