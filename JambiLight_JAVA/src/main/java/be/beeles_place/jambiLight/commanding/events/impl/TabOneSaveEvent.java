package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class TabOneSaveEvent extends BaseEvent {

    //Content
    public TextField T1_TXT_VerticalLeds;
    public TextField T1_TXT_HorizontalLeds;
    public TextField T1_TXT_TotalLeds;
    public Slider T1_SLD_VerticalMarg;
    public Slider T1_SLD_HorizontalMarg;
}
