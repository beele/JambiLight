package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

import java.util.function.Consumer;

public class TabThreeSaveEvent extends BaseEvent {

    //Content:
    public Slider T3_SLD_PixelStepSize;
    public CheckBox T3_CHK_Weighing;
    public Slider T3_SLD_WeighFactor;
    public CheckBox T3_CHK_Interpolation;
    public Slider T3_SLD_Interpolation;

    //Fields:
    private Consumer<String> errorCallback;

    //Getters & setters:
    public Consumer<String> getErrorCallback() {
        return errorCallback;
    }

    public void setErrorCallback(Consumer<String> errorCallback) {
        this.errorCallback = errorCallback;
    }
}