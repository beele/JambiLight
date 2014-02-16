package be.beeles_place.view;

import be.beeles_place.events.ShowPreferencesEvent;
import be.beeles_place.events.ShutdownEvent;
import be.beeles_place.model.ColorModel;
import be.beeles_place.model.SettingsModel;
import be.beeles_place.utils.EventbusWrapper;
import com.google.common.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    //Local variables.
    private EventBus eventBus;
    private ColorModel model;
    private SettingsModel settings;

    //FXML items.
    @FXML
    private GridPane gridItems;

    @FXML
    private AnchorPane imageContainer;

    private List<Pane> panes;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        //Register this class to receive events from the event bus!
        eventBus = EventbusWrapper.getInstance();
        eventBus.register(this);
    }

    public void initUI() {
        //Dynamically generate the columns and rows.
        for(int i = 0; i < settings.getHorizontalRegions() - 1 ; i++) {
            ColumnConstraints ccs = new ColumnConstraints(10D,100D,-1D,Priority.SOMETIMES,null,true);
            gridItems.getColumnConstraints().add(ccs);
        }
        for(int j = 0; j < settings.getVerticalRegions() - 1 ; j++) {
            RowConstraints rcs = new RowConstraints(10D,30D,-1D,Priority.SOMETIMES,null,true);
            gridItems.getRowConstraints().add(rcs);
        }
        //Set the imageContainer to the correct position.
        GridPane.setColumnIndex(imageContainer,1);
        GridPane.setRowIndex(imageContainer,1);
        GridPane.setColumnSpan(imageContainer, settings.getHorizontalRegions() - 2);
        GridPane.setRowSpan(imageContainer, settings.getVerticalRegions() - 2);
        //Add the panels to the UI.
        addPanels();
    }

    private void addPanels() {
        panes = new ArrayList<>();

        for (int i = 0; i < settings.getHorizontalRegions(); i++) {
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: rgb(0,255," + (i * 10 + 10) + ");");
            panes.add(pane);
            gridItems.add(pane, i, 0);
        }

        for (int i = 0; i < settings.getVerticalRegions() - 2; i++) {
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: rgb(0,255," + (i * 10 + 10) + ");");
            panes.add(pane);
            gridItems.add(pane, settings.getHorizontalRegions() - 1, i + 1);
        }

        for (int i = 0; i < settings.getHorizontalRegions(); i++) {
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: rgb(0,255," + (i * 10 + 10) + ");");
            panes.add(pane);
            gridItems.add(pane, (settings.getHorizontalRegions() - 1) - i, settings.getVerticalRegions() - 1);
        }

        for (int i = 0; i < settings.getVerticalRegions() - 2; i++) {
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: rgb(0,255," + (i * 10 + 10) + ");");
            panes.add(pane);
            gridItems.add(pane, 0, (settings.getVerticalRegions() - 2) - i);
        }
    }

    public void updateColors() {
        int[][] colors = model.getCurrentColors();

        for (int i = 0; i < panes.size(); i++) {
            int[] rgb = colors[i];
            String values = "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ");";
            panes.get(i).setStyle("-fx-background-color: " + values);
            values = null;
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

    public void setSettings(SettingsModel settings) {
        this.settings = settings;
    }

    public SettingsModel getSettings() {
        return settings;
    }
}