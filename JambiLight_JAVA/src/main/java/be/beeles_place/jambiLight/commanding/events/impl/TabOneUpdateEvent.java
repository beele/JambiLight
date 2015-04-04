package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TabOneUpdateEvent extends BaseEvent {

    public TabOneUpdateEvent(Stage stage) {
        Scene scene = stage.getScene();

        T1_TXT_VerticalLeds = (TextField) scene.lookup("#T1_TXT_VerticalLeds");
        T1_TXT_HorizontalLeds = (TextField) scene.lookup("#T1_TXT_HorizontalLeds");
        T1_TXT_TotalLeds = (TextField) scene.lookup("#T1_TXT_TotalLeds");
        T1_SLD_VerticalMarg = (Slider) scene.lookup("#T1_SLD_VerticalMarg");
        T1_SLD_HorizontalMarg = (Slider) scene.lookup("#T1_SLD_HorizontalMarg");
    }

    //Content
    public TextField T1_TXT_VerticalLeds;
    public TextField T1_TXT_HorizontalLeds;
    public TextField T1_TXT_TotalLeds;
    public Slider T1_SLD_VerticalMarg;
    public Slider T1_SLD_HorizontalMarg;
}