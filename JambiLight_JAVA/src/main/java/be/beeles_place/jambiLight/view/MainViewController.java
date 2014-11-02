package be.beeles_place.jambiLight.view;

import be.beeles_place.jambiLight.events.SettingsUpdatedEvent;
import be.beeles_place.jambiLight.events.ShutdownEvent;
import be.beeles_place.jambiLight.events.VisualDebugEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.EventbusWrapper;
import be.beeles_place.jambiLight.utils.screenCapture.ScreenCapperStrategy;
import com.google.common.eventbus.EventBus;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.controlsfx.dialog.Dialogs;

import java.net.URL;
import java.util.*;

//TODO: Better error handling and new settings UI.
public class MainViewController implements Initializable {

    //Local variables.
    private EventBus eventBus;
    private ColorModel model;
    private SettingsModel settings;

    private Image image;
    private double cellWidth;
    private double cellHeight;

    @FXML
    private Canvas ledCanvas;

    @FXML
    private AnchorPane canvasWrapper;

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
    private ComboBox<ScreenCapperStrategy> cmbColorMode;

    @FXML
    private Button btnVisualDebug;

    /**
     * Executed when the view is initialized.
     *
     * @param fxmlFileLocation The location of the FXML file.
     * @param resources Resource bundle if any.
     */
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        //Register this class to receive events from the event bus!
        eventBus = EventbusWrapper.getInstance();
        eventBus.register(this);

        image = new Image(getClass().getResourceAsStream("/be/beeles_place/jambiLight/view/assets/tv.png"));
    }

    /**
     * Initializes the UI.
     */
    public void initUI() {
        canvasWrapper.widthProperty().addListener((observableValue, oldValue, newValue) -> drawUI());
        canvasWrapper.heightProperty().addListener((observableValue, oldValue, newValue) -> drawUI());

        //Set the default opened settings panel.
        settingsPanes.expandedPaneProperty().setValue(firstSettingsPane);

        drawUI();
        updateSettingsValues();
    }

    private void drawUI() {
        ledCanvas.setWidth(canvasWrapper.getWidth());
        ledCanvas.setHeight(canvasWrapper.getHeight());

        cellWidth = ledCanvas.getWidth() / settings.getHorizontalRegions();
        cellHeight = ledCanvas.getHeight() / settings.getVerticalRegions();

        GraphicsContext gc = ledCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, ledCanvas.getWidth(), ledCanvas.getHeight());
        gc.drawImage(image, cellWidth, cellHeight, ledCanvas.getWidth() - cellWidth * 2, ledCanvas.getHeight() - cellHeight * 2);

        int totalRegions = settings.getHorizontalRegions() * 2 + (settings.getVerticalRegions() - 2) * 2;
        for (int i = 0 ; i < totalRegions ; i++) {
            Color c;
            if(i % 2 == 0) {
                c = Color.GREEN;
            } else {
                c = Color.RED;
            }
            drawCell(i, cellWidth, cellHeight, c, ledCanvas);
        }
    }

    private void drawCell(int number, double cellWidth, double cellHeight, Color color, Canvas canvas) {
        int x = (int)(canvas.getWidth() / 2 - cellWidth / 2);
        int y = (int)(canvas.getHeight() / 2 - cellHeight / 2);

        int h1 = settings.getHorizontalRegions();
        int v1 = h1 + settings.getVerticalRegions() - 1;
        int h2 = v1 + settings.getHorizontalRegions() - 2;
        int v2 = h2 + settings.getVerticalRegions();

        if(number < h1) {
            x = (int)(number * cellWidth);
            y = 0;
        } else if(number >= h1 && number < v1) {
            x = (int)(canvas.getWidth() - cellWidth);
            y = (int)((number - h1 + 1) * cellHeight);
        } else if(number >= v1 && number < h2) {
            x = (int)(canvas.getWidth() - cellWidth * (number - v1 + 2));
            y = (int)(canvas.getHeight() - cellHeight);
        } else if(number >= h2 && number < v2) {
            x = 0;
            y = (int)(canvas.getHeight() - cellHeight * (number - h2 + 1));
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(x, y, cellWidth, cellHeight);
    }

    /**
     * Re-initializes the UI.
     */
    private void reInitUI() {
        drawUI();
        updateSettingsValues();
    }

    /**
     * Update the colors.
     */
    public void updateColors() {
        int[][] colors = model.getCurrentColors();

        for (int i = 0; i < colors.length; i++) {
            int[] rgb = colors[i];
            drawCell(i, cellWidth, cellHeight, new Color((double) rgb[0] / 255, (double) rgb[1] / 255, (double) rgb[2] / 255, 1), ledCanvas);
            rgb = null;
        }

        colors = null;
    }

    /**
     * Updates view to show the new settings values.
     */
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

        cmbColorMode.setItems(FXCollections.observableArrayList(new ArrayList<ScreenCapperStrategy>(Arrays.asList(ScreenCapperStrategy.values()))));
        if(settings.getCaptureMode() != null) {
            cmbColorMode.getSelectionModel().select(settings.getCaptureMode());
        }
    }

    /**
     * Show an error popup message with the given title and message.
     *
     * @param title The title to display.
     * @param message The message to display.
     */
    private void showErrorMessage(String title, String message) {
        Dialogs.create()
                .owner(canvasWrapper)
                .title(title)
                .message(message)
                .lightweight()
                .showError();
    }

    //Event handlers.
    @FXML
    void OnSaveSettingsClicked(ActionEvent event) {
        int hRegions;
        int vRegions;
        try {
            hRegions = Integer.parseInt(txtHorizontalRegions.getText());
            vRegions = Integer.parseInt(txtVerticalRegions.getText());
            if(hRegions < 3 || vRegions < 3) {
                throw new Exception("The minimal dimensions required are 3 horizontal and 3 vertical regions!");
            }
        } catch (Exception e) {
            showErrorMessage("Cannot save settings!", e.getMessage());
            return;
        }

        //Only save settings when no errors have occurred!
        settings.setPort(cmbSerialPort.getSelectionModel().getSelectedItem());
        settings.setAutoConnect(chkAutoConntect.selectedProperty().getValue());
        settings.setHorizontalRegions(hRegions);
        settings.setVerticalRegions(vRegions);
        settings.setHorizontalMargin((int)sldHorizontalMargin.getValue());
        settings.setVerticalMargin((int)sldVerticalMargin.getValue());
        settings.setPixelIteratorStepSize((int)sldPixelStepSize.getValue());

        eventBus.post(new SettingsUpdatedEvent());
        //Update the view again => recalculate new boundaries.
        reInitUI();
    }

    @FXML
    void OnSaveColorWeightSettingsClicked(ActionEvent event) {
        int weighFactor;

        try {
            weighFactor = Integer.parseInt(txtColorWeight.getText());
            if(weighFactor < 1 || weighFactor > 5) {
                throw new Exception("Weight factor should be in range of [1 , 5]");
            }
        } catch (Exception e) {
            showErrorMessage("Cannot save settings!", e.getMessage());
            return;
        }

        //Only save settings when no errors have occurred!
        settings.setWeighColor(chkWeighColors.selectedProperty().getValue());
        settings.setWeighFactor(weighFactor);

        eventBus.post(new SettingsUpdatedEvent());
    }

    @FXML
    void OnSaveEnhancementSettingsClicked(ActionEvent event) {
        float enhanceValue;
        int gThreshold;
        float scaleUp;
        float scaleDown;

        try {
            enhanceValue = Float.parseFloat(txtEnhanceFactor.getText());
            if(enhanceValue < 1f || enhanceValue > 10f) {
                throw new Exception("Color enhance value should be in range of [1 , 10]");
            }

            gThreshold = Integer.parseInt(txtGreyThreshold.getText());
            scaleUp = Float.parseFloat(txtScaleUpValue.getText());
            scaleDown = Float.parseFloat(txtScaleDownValue.getText());
            if(gThreshold < 0 || scaleUp < 0 || scaleDown < 0) {
                throw new Exception("Threshold, scale-up and scale-down should be greater than 0!");
            }
        } catch (Exception e) {
            showErrorMessage("Cannot save settings!", e.getMessage());
            return;
        }

        //Only save settings when no errors have occurred!
        settings.setEnhanceColor(chkEnhanceColors.selectedProperty().getValue());
        settings.setEnhanceValue(enhanceValue);
        settings.setCorrectIntensity(chkIntensifyColors.selectedProperty().getValue());
        settings.setGreyDetectionThreshold(gThreshold);
        settings.setScaleUpValue(scaleUp);
        settings.setScaleDownValue(scaleDown);

        eventBus.post(new SettingsUpdatedEvent());
    }

    @FXML
    void OnSaveAdvancedSettingsClicked(ActionEvent event) {
        settings.setCaptureMode(cmbColorMode.getValue());

        eventBus.post(new SettingsUpdatedEvent());
    }

    @FXML
    void onShowDebugClicked(ActionEvent event) {
        if(btnVisualDebug.getText().equals("Start debug")) {
            btnVisualDebug.setText("Stop debug");
            eventBus.post(new VisualDebugEvent(true));
        } else {
            btnVisualDebug.setText("Start debug");
            eventBus.post(new VisualDebugEvent(false));
        }
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