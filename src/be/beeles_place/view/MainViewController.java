package be.beeles_place.view;

import be.beeles_place.events.ShowPreferencesEvent;
import be.beeles_place.events.ShutdownEvent;
import be.beeles_place.model.ColorModel;
import be.beeles_place.utils.EventbusWrapper;
import com.google.common.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    //Local variables.
    private EventBus eventBus;
    private ColorModel model;

    //FXML items.
    @FXML
    private GridPane gridItems;

    private List<Pane> panes;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        //Register this class to receive events from the event bus!
        eventBus = EventbusWrapper.getInstance();
        eventBus.register(this);

        //Add the panels to the UI.
        addPanels();
    }

    public void addPanels() {
        panes = new ArrayList<>();

        for (int i = 0; i < 16; i++) {
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: rgb(0,255," + (i * 10 + 10) + ");");
            panes.add(pane);
            gridItems.add(pane, i, 0);
        }

        for (int i = 0; i < 8; i++) {
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: rgb(0,255," + (i * 10 + 10) + ");");
            panes.add(pane);
            gridItems.add(pane, 15, i + 1);
        }

        for (int i = 0; i < 16; i++) {
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: rgb(0,255," + (i * 10 + 10) + ");");
            panes.add(pane);
            gridItems.add(pane, 15 - i, 9);
        }

        for (int i = 0; i < 8; i++) {
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: rgb(0,255," + (i * 10 + 10) + ");");
            panes.add(pane);
            gridItems.add(pane, 0, 8 - i);
        }
    }

    public void updateColors() {
        int[][] colors = model.getCurrentColors();

        for (int i = 0; i < panes.size(); i++) {
            int[] rgb = colors[i];
            String values = "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ");";
            panes.get(i).setStyle("-fx-background-color: " + values);
        }
    }

    //Event handlers.
    @FXML
    void onPreferencesClicked(ActionEvent event) {
        eventBus.post(new ShowPreferencesEvent());
    }

    @FXML
    void OnCloseClicked(ActionEvent event) {
        eventBus.post(new ShutdownEvent());
    }

    //Getters & setters.
    public ColorModel getModel() {
        return model;
    }

    public void setModel(ColorModel model) {
        this.model = model;
    }
}