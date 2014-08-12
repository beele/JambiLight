package be.beeles_place.jambiLight.view;

import be.beeles_place.jambiLight.events.SettingsUpdatedEvent;
import be.beeles_place.jambiLight.events.ShutdownEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.EventbusWrapper;
import be.beeles_place.jambiLight.utils.screenCapture.ScreenCapperMode;
import com.google.common.eventbus.EventBus;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.*;

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

    @FXML
    private Accordion settingsPanes;

    @FXML
    private TitledPane firstSettingsPane;

    //First settings page
    @FXML
    private ComboBox<String> cmbSerialPort;

    @FXML
    private CheckBox chkAutoConntect;

    @FXML
    private Slider sldVerticalMargin;

    @FXML
    private Slider sldPixelStepSize;

    @FXML
    private TextField txtHorizontalRegions;

    @FXML
    private TextField txtVerticalRegions;

    @FXML
    private Slider sldHorizontalMargin;

    //Second settings page.
    @FXML
    private CheckBox chkWeighColors;

    @FXML
    private TextField txtColorWeight;

    //Third settings page.
    @FXML
    private CheckBox chkEnhanceColors;

    @FXML
    private TextField txtEnhanceFactor;

    @FXML
    private CheckBox chkIntensifyColors;

    @FXML
    private TextField txtGreyThreshold;

    @FXML
    private TextField txtScaleUpValue;

    @FXML
    private TextField txtScaleDownValue;

    @FXML
    private ComboBox<ScreenCapperMode> cmbColorMode;

    //Local variables
    private final List<Rectangle> rects = new ArrayList<>();
    private final ColumnConstraints ccs = new ColumnConstraints(10D,100D,-1D,Priority.SOMETIMES,null,true);
    private final RowConstraints rcs = new RowConstraints(10D,30D,-1D,Priority.SOMETIMES,null,true);

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        //Register this class to receive events from the event bus!
        eventBus = EventbusWrapper.getInstance();
        eventBus.register(this);
    }

    public void initUI() {
        //Dynamically generate the columns and rows.
        for(int i = 0; i < settings.getHorizontalRegions() - 1; i++) {
            gridItems.getColumnConstraints().add(ccs);
        }
        for(int j = 0; j < settings.getVerticalRegions() - 1; j++) {
            gridItems.getRowConstraints().add(rcs);
        }
        //Set the imageContainer to the correct position.
        GridPane.setColumnIndex(imageContainer,1);
        GridPane.setRowIndex(imageContainer,1);
        GridPane.setColumnSpan(imageContainer, settings.getHorizontalRegions() - 2);
        GridPane.setRowSpan(imageContainer, settings.getVerticalRegions() - 2);

        //Set the default opened settings panel.
        settingsPanes.expandedPaneProperty().setValue(firstSettingsPane);

        //Add the panels to the UI.
        addPanels();
        updateSettingsValues();
    }

    private void reInitUI() {
        for(int i = 0; i < settings.getHorizontalRegions(); i++) {
            gridItems.getColumnConstraints().add(ccs);
        }
        for(int j = 0; j < settings.getVerticalRegions(); j++) {
            gridItems.getRowConstraints().add(rcs);
        }

        GridPane.setColumnSpan(imageContainer, settings.getHorizontalRegions() - 2);
        GridPane.setRowSpan(imageContainer, settings.getVerticalRegions() - 2);

        //Add the panels to the UI.
        addPanels();
        updateSettingsValues();
    }

    private void addPanels() {
        rects.removeAll(rects);

        for (int i = 0; i < settings.getHorizontalRegions(); i++) {
            Rectangle rect = new Rectangle();
            rect.widthProperty().bind(gridItems.widthProperty().divide(20));
            rect.heightProperty().bind(gridItems.heightProperty().divide(14));
            rect.setFill(new Color(0,1,(i * 10 + 10)/(double)255,1));
            rects.add(rect);
            gridItems.add(rect, i, 0);
        }

        for (int i = 0; i < settings.getVerticalRegions() - 2; i++) {
            Rectangle rect = new Rectangle();
            rect.widthProperty().bind(gridItems.widthProperty().divide(20));
            rect.heightProperty().bind(gridItems.heightProperty().divide(14));
            rect.setFill(new Color(0,1,(i * 10 + 10)/(double)255,1));
            rects.add(rect);
            gridItems.add(rect, settings.getHorizontalRegions() - 1, i + 1);
        }

        for (int i = 0; i < settings.getHorizontalRegions(); i++) {
            Rectangle rect = new Rectangle();
            rect.widthProperty().bind(gridItems.widthProperty().divide(20));
            rect.heightProperty().bind(gridItems.heightProperty().divide(14));
            rect.setFill(new Color(0,1,(i * 10 + 10)/(double)255,1));
            rects.add(rect);
            gridItems.add(rect, (settings.getHorizontalRegions() - 1) - i, settings.getVerticalRegions() - 1);
        }

        for (int i = 0; i < settings.getVerticalRegions() - 2; i++) {
            Rectangle rect = new Rectangle();
            rect.widthProperty().bind(gridItems.widthProperty().divide(20));
            rect.heightProperty().bind(gridItems.heightProperty().divide(14));
            rect.setFill(new Color(0,1,(i * 10 + 10)/(double)255,1));
            rects.add(rect);
            gridItems.add(rect, 0, (settings.getVerticalRegions() - 2) - i);
        }
    }

    private void rebuildGrid() {
        ListIterator iter = gridItems.getChildren().listIterator();
        while(iter.hasNext()) {
            Object e = iter.next();
            if(e instanceof AnchorPane == false) {
                iter.remove();
            }
        }
        iter = gridItems.getColumnConstraints().listIterator();
        while(iter.hasNext()) {
            iter.next();
            iter.remove();
        }
        iter = gridItems.getRowConstraints().listIterator();
        while(iter.hasNext()) {
            iter.next();
            iter.remove();
        }
        rects.removeAll(rects);

        reInitUI();
    }

    public void updateColors() {
        int[][] colors = model.getCurrentColors();

        //This prevents an ArrayIndexOutOfBoundsException when the size of the regions has just been changed!
        if(colors.length == rects.size()) {
            for (int i = 0; i < rects.size(); i++) {
                int[] rgb = colors[i];
                rects.get(i).setFill(new Color((double)rgb[0]/255,(double)rgb[1]/255,(double)rgb[2]/255,1));
                rgb = null;
            }
        }

        colors = null;
    }

    private void updateSettingsValues() {
        cmbSerialPort.setItems(FXCollections.observableList(settings.getPorts()));
        if(settings.getPort() != null) {
            cmbSerialPort.getSelectionModel().select(settings.getPort());
        }
        chkAutoConntect.setSelected(settings.isAutoConnect());

        txtHorizontalRegions.setText(settings.getHorizontalRegions() + "");
        txtVerticalRegions.setText(settings.getVerticalRegions() + "");

        sldHorizontalMargin.setMax(settings.getHorizontalRegions() / 2);
        sldHorizontalMargin.setValue(settings.getHorizontalMargin());
        sldVerticalMargin.setMax(settings.getVerticalRegions() / 2);
        sldVerticalMargin.setValue(settings.getVerticalMargin());

        sldPixelStepSize.setValue(settings.getPixelIteratorStepSize());

        chkWeighColors.setSelected(settings.isWeighColor());
        txtColorWeight.setText(settings.getWeighFactor() + "");

        chkEnhanceColors.setSelected(settings.isEnhanceColor());
        txtEnhanceFactor.setText(settings.getEnhanceValue() + "");

        chkIntensifyColors.setSelected(settings.isCorrectIntensity());
        txtGreyThreshold.setText(settings.getGreyDetectionThreshold() + "");
        txtScaleUpValue.setText(settings.getScaleUpValue() + "");
        txtScaleDownValue.setText(settings.getScaleDownValue() + "");

        cmbColorMode.setItems(FXCollections.observableArrayList(new ArrayList<ScreenCapperMode>(Arrays.asList(ScreenCapperMode.values()))));
        if(settings.getCaptureMode() != null) {
            cmbColorMode.getSelectionModel().select(settings.getCaptureMode());
        }
    }

    //Event handlers.
    @FXML
    void OnSaveSettingsClicked(ActionEvent event) {
        settings.setPort(cmbSerialPort.getSelectionModel().getSelectedItem());
        settings.setAutoConnect(chkAutoConntect.selectedProperty().getValue());

        try {
            int hRegions = Integer.parseInt(txtHorizontalRegions.getText());
            int vRegions = Integer.parseInt(txtVerticalRegions.getText());
            if(hRegions < 0 || vRegions < 0) {
                throw new Exception("TODO-Exception");
            }
            settings.setHorizontalRegions(hRegions);
            settings.setVerticalRegions(vRegions);
        } catch (Exception e) {
            //TODO: handle this!
        }

        settings.setHorizontalMargin((int)sldHorizontalMargin.getValue());
        settings.setVerticalMargin((int)sldVerticalMargin.getValue());

        settings.setPixelIteratorStepSize((int)sldPixelStepSize.getValue());

        eventBus.post(new SettingsUpdatedEvent());
        //Update the view again => recalculate new boundaries.
        rebuildGrid();
    }

    @FXML
    void OnSaveColorWeightSettingsClicked(ActionEvent event) {
        settings.setWeighColor(chkWeighColors.selectedProperty().getValue());
        try {
            int weighFactor = Integer.parseInt(txtColorWeight.getText());
            if(weighFactor < 1 || weighFactor > 5) {
                throw new Exception("TODO-Exception");
            }
            settings.setWeighFactor(weighFactor);
        } catch (Exception e){
            //TODO: handle this!
        }

        eventBus.post(new SettingsUpdatedEvent());
    }

    @FXML
    void OnSaveEnhancementSettingsClicked(ActionEvent event) {
        settings.setEnhanceColor(chkEnhanceColors.selectedProperty().getValue());
        try {
            float enhanceValue = Float.parseFloat(txtEnhanceFactor.getText());
            if(enhanceValue < 1f || enhanceValue > 10f) {
                throw new Exception("TODO-Exception");
            }
            settings.setEnhanceValue(enhanceValue);
        } catch (Exception e) {
            //TODO: handle this!
        }

        settings.setCorrectIntensity(chkIntensifyColors.selectedProperty().getValue());
        try {
            int gThreshold = Integer.parseInt(txtGreyThreshold.getText());
            float scaleUp = Float.parseFloat(txtScaleUpValue.getText());
            float scaleDown = Float.parseFloat(txtScaleDownValue.getText());
            if(gThreshold < 0 || scaleUp < 0 || scaleDown < 0) {
                throw new Exception("TODO-Exception");
            }
            settings.setGreyDetectionThreshold(gThreshold);
            settings.setScaleUpValue(scaleUp);
            settings.setScaleDownValue(scaleDown);
        } catch (Exception e) {
            //TODO: handle this!
        }

        eventBus.post(new SettingsUpdatedEvent());
    }

    @FXML
    void OnSaveAdvancedSettingsClicked(ActionEvent event) {
        settings.setCaptureMode(cmbColorMode.getValue());

        eventBus.post(new SettingsUpdatedEvent());
    }

    @FXML
    private void OnCloseClicked(ActionEvent event) {
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