package be.beeles_place.jambiLight.commanding.events.impl;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TabSixUpdateEvent extends BaseEvent {

    public TabSixUpdateEvent(Stage stage, Image image) {
        Scene scene = stage.getScene();

        this.image = image;
        T6_LedCanvas = (Canvas) scene.lookup("#T6_LedCanvas");
        T6_LBL_StatusInfo = (Label) scene.lookup("#T6_LBL_StatusInfo");
    }

    //Content:
    public Image image;
    public Canvas T6_LedCanvas;
    public Label T6_LBL_StatusInfo;
}