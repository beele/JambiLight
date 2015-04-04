package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class TabThreeUpdateEvent extends BaseEvent {

    public TabThreeUpdateEvent(Stage stage) {
        Scene scene = stage.getScene();

        T3_SLD_PixelStepSize = (Slider) scene.lookup("#T3_SLD_PixelStepSize");
        T3_CHK_Weighing = (CheckBox) scene.lookup("#T3_CHK_Weighing");
        T3_SLD_WeighFactor = (Slider) scene.lookup("#T3_SLD_WeighFactor");
        T3_CHK_Interpolation = (CheckBox) scene.lookup("#T3_CHK_Interpolation");
        T3_SLD_Interpolation = (Slider) scene.lookup("#T3_SLD_Interpolation");
    }

    //Content:
    public Slider T3_SLD_PixelStepSize;
    public CheckBox T3_CHK_Weighing;
    public Slider T3_SLD_WeighFactor;
    public CheckBox T3_CHK_Interpolation;
    public Slider T3_SLD_Interpolation;
}