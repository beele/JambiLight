package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.ScreenCapperStrategy;
import javafx.scene.control.ComboBox;

import java.util.function.Consumer;

public class TabTwoSaveEvent extends BaseEvent {

    //Content:
    public ComboBox<ScreenCapperStrategy> T2_CMB_CaptureMode;
    public ComboBox<String> T2_CMB_DirectShowDevices;
}