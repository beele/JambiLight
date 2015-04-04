package be.beeles_place.jambiLight.view;

import be.beeles_place.jambiLight.commanding.CommandMapper;
import be.beeles_place.jambiLight.commanding.events.impl.*;
import be.beeles_place.jambiLight.commanding.events.application.UpdateUserInterfaceEvent;
import be.beeles_place.jambiLight.commanding.events.application.VisualDebugEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.ArduinoCode;
import be.beeles_place.jambiLight.commanding.EventbusWrapper;
import be.beeles_place.jambiLight.utils.StageFactory;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.DirectShowEnumerator;
import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.ScreenCapperStrategy;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.controlsfx.dialog.Dialogs;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainViewController implements Initializable {

    //Constants
    private final String T1 = "T1_Screen";
    private final String T2 = "T2_Function";
    private final String T3 = "T3_Settings";
    private final String T4 = "T4_Advanced";
    private final String T5 = "T5_Arduino";
    private final String T6 = "T6_Debug";
    private final String T7 = "T7_Info";

    //View state tracker
    private String currentTab = null;

    //Components
    @FXML
    private Button SIDE_BTN_Screen;
    @FXML
    private Button SIDE_BTN_Function;
    @FXML
    private Button SIDE_BTN_Settings;
    @FXML
    private Button SIDE_BTN_Advanced;
    @FXML
    private Button SIDE_BTN_Arduino;
    @FXML
    private Button SIDE_BTN_Debug;
    @FXML
    private Button SIDE_BTN_Info;

    @FXML
    private StackPane STCK_TabContainer;

    @FXML
    private Canvas T6_LedCanvas;

    //Local variables
    private Image image;
    private double cellWidth;
    private double cellHeight;

    //Application variables
    private EventBus eventBus;
    private ColorModel model;
    private SettingsModel settings;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        //Register this class to receive events from the event bus!
        eventBus = EventbusWrapper.getInstance();
        eventBus.register(this);

        image = new Image(getClass().getResourceAsStream("/be/beeles_place/jambiLight/view/old/assets/tv_new.png"));
    }

    public void initUI(ColorModel model, SettingsModel settings) {
        this.settings = settings;
        this.model = model;

        //Activate the first tab!
        currentTab = T1;
        setActiveStackView(T1);
        updateTabOne();
    }

    //#########################################
    //### Event handlers.
    //#########################################
    @Subscribe
    public void onUpdateOfInterfaceRequired(UpdateUserInterfaceEvent event) {
        //Only update if the tab is visible!
        if(T6.equals(currentTab)) {
            updateTabSix();
        }
    }

    @FXML
    void onSideButtonClicked(ActionEvent event) {
        Button target = (Button) event.getTarget();

        //Figure out which button in the sidebar was clicked and change the stack's first child to the corresponding view.
        if(SIDE_BTN_Screen.equals(target)) {
            currentTab = T1;
            setActiveStackView(T1);
            updateTabOne();
        } else if(SIDE_BTN_Function.equals(target)) {
            currentTab = T2;
            setActiveStackView(T2);
            updateTabTwo();
        } else if(SIDE_BTN_Settings.equals(target)) {
            currentTab = T3;
            setActiveStackView(T3);
            updateTabThree();
        } else if(SIDE_BTN_Advanced.equals(target)) {
            currentTab = T4;
            setActiveStackView(T4);
            updateTabFour();
        } else if(SIDE_BTN_Arduino.equals(target)) {
            currentTab = T5;
            setActiveStackView(T5);
            updateTabFive();
        } else if(SIDE_BTN_Debug.equals(target)) {
            currentTab = T6;
            setActiveStackView(T6);
            drawDebugUI();
        } else if(SIDE_BTN_Info.equals(target)) {
            currentTab = T7;
            setActiveStackView(T7);
        }
    }

    @FXML
    private TextField T1_TXT_VerticalLeds;
    @FXML
    private TextField T1_TXT_HorizontalLeds;
    @FXML
    private TextField T1_TXT_TotalLeds;
    @FXML
    private Slider T1_SLD_VerticalMarg;
    @FXML
    private Slider T1_SLD_HorizontalMarg;

    private void updateTabOne() {
        T1_TXT_VerticalLeds.setText((settings.getVerticalRegions() - 2) + "");
        T1_TXT_HorizontalLeds.setText(settings.getHorizontalRegions() + "");

        T1_TXT_TotalLeds.setText(((settings.getVerticalRegions() * 2 + settings.getHorizontalRegions() * 2) - 4) + "");

        T1_SLD_VerticalMarg.setValue(settings.getVerticalMargin());
        T1_SLD_HorizontalMarg.setValue(settings.getHorizontalMargin());
    }

    @FXML
    void onTabOneSaveClicked(ActionEvent event) {
        try {
            //TODO: Would it not be better to pass the scene to the command and have the command get all the components by id?
            //TODO: stage.getScene().lookup("FX_ID");

            TabOneSaveEvent evt = new TabOneSaveEvent();
            evt.T1_TXT_VerticalLeds = T1_TXT_VerticalLeds;
            evt.T1_TXT_HorizontalLeds = T1_TXT_HorizontalLeds;
            evt.T1_TXT_TotalLeds = T1_TXT_TotalLeds;
            evt.T1_SLD_VerticalMarg = T1_SLD_VerticalMarg;
            evt.T1_SLD_HorizontalMarg = T1_SLD_HorizontalMarg;

            evt.setCallback(this::updateTabOne);
            evt.setErrorCallback((errorMessages) -> showErrorMessage(errorMessages[0], errorMessages[1]));

            CommandMapper.getInstance().dispatchEvent(evt);
        } catch (Exception e) {
            showErrorMessage("General error", e.getMessage());
        }
    }

    @FXML
    private ComboBox<ScreenCapperStrategy> T2_CMB_CaptureMode;
    @FXML
    private ComboBox<String> T2_CMB_DirectShowDevices;

    private void updateTabTwo() {
        T2_CMB_CaptureMode.setItems(FXCollections.observableArrayList(new ArrayList<ScreenCapperStrategy>(Arrays.asList(ScreenCapperStrategy.values()))));
        if(settings.getCaptureMode() != null) {
            T2_CMB_CaptureMode.getSelectionModel().select(settings.getCaptureMode());
        }

        //Bindings to disable parts of the UI if required.
        T2_CMB_DirectShowDevices.disableProperty().bind(T2_CMB_CaptureMode.getSelectionModel().selectedItemProperty().isNotEqualTo(ScreenCapperStrategy.DIRECT_SHOW));

        //Get devices and make them into a list.
        List<String> devices = new ArrayList<>();
        devices.addAll(DirectShowEnumerator.enumerateDirectShowDevices().values().stream().collect(Collectors.toList()));

        T2_CMB_DirectShowDevices.setItems(FXCollections.observableArrayList(devices));
        if(settings.getDirectShowDeviceName() != null) {
            T2_CMB_DirectShowDevices.getSelectionModel().select(settings.getDirectShowDeviceName());
        }
    }

    @FXML
    void onTabTwoSaveClicked(ActionEvent event) {
        try {
            TabTwoSaveEvent evt = new TabTwoSaveEvent();
            evt.T2_CMB_CaptureMode = T2_CMB_CaptureMode;
            evt.T2_CMB_DirectShowDevices = T2_CMB_DirectShowDevices;

            evt.setCallback(this::updateTabTwo);
            evt.setErrorCallback((errorMessages) -> showErrorMessage(errorMessages[0], errorMessages[1]));

            CommandMapper.getInstance().dispatchEvent(evt);
        } catch (Exception e) {
            showErrorMessage("General error", e.getMessage());
        }
    }

    @FXML
    private Slider T3_SLD_PixelStepSize;
    @FXML
    private CheckBox T3_CHK_Weighing;
    @FXML
    private Slider T3_SLD_WeighFactor;
    @FXML
    private CheckBox T3_CHK_Interpolation;
    @FXML
    private Slider T3_SLD_Interpolation;
    
    private void updateTabThree() {
        T3_SLD_PixelStepSize.setValue(settings.getPixelIteratorStepSize());

        T3_CHK_Weighing.setSelected(settings.isWeighColor());
        T3_SLD_WeighFactor.setValue(settings.getWeighFactor());

        T3_CHK_Interpolation.setSelected(settings.isInterpolated());
        T3_SLD_Interpolation.setValue(settings.getInterpolation());

        //Bindings to disable parts of the UI if required.
        T3_SLD_WeighFactor.disableProperty().bind(T3_CHK_Weighing.selectedProperty().not());
        T3_SLD_Interpolation.disableProperty().bind(T3_CHK_Interpolation.selectedProperty().not());
    }

    @FXML
    void onTabThreeSaveClicked(ActionEvent event) {
        try {
            TabThreeSaveEvent evt = new TabThreeSaveEvent();
            evt.T3_SLD_PixelStepSize = T3_SLD_PixelStepSize;
            evt.T3_CHK_Weighing = T3_CHK_Weighing;
            evt.T3_SLD_WeighFactor = T3_SLD_WeighFactor;
            evt.T3_CHK_Interpolation = T3_CHK_Interpolation;
            evt.T3_SLD_Interpolation = T3_SLD_Interpolation;

            evt.setCallback(this::updateTabThree);
            evt.setErrorCallback((errorMessages) -> showErrorMessage(errorMessages[0], errorMessages[1]));

            CommandMapper.getInstance().dispatchEvent(evt);
        } catch (Exception e) {
            showErrorMessage("General error", e.getMessage());
        }
    }

    @FXML
    private CheckBox T4_CHK_EnhanceColors;
    @FXML
    private TextField T4_TXT_EnhancementValue;
    @FXML
    private CheckBox T4_CHK_EnhancePerChannel;
    @FXML
    private TextField T4_TXT_ChannelRed;
    @FXML
    private TextField T4_TXT_ChannelGreen;
    @FXML
    private TextField T4_TXT_ChannelBlue;
    @FXML
    private CheckBox T4_CHK_CorrectIntensity;
    @FXML
    private TextField T4_TXT_GreyThreshold;
    @FXML
    private TextField T4_TXT_ScaleUp;
    @FXML
    private TextField T4_TXT_ScaleDown;

    private void updateTabFour() {
        T4_CHK_EnhanceColors.setSelected(settings.isEnhanceColor());
        T4_TXT_EnhancementValue.setText(settings.getEnhanceValue() + "");

        T4_CHK_EnhancePerChannel.setSelected(settings.isEnhancePerChannel());
        T4_TXT_ChannelRed.setText(settings.getEnhanceValueR() + "");
        T4_TXT_ChannelGreen.setText(settings.getEnhanceValueG() + "");
        T4_TXT_ChannelBlue.setText(settings.getEnhanceValueB() + "");

        T4_CHK_CorrectIntensity.setSelected(settings.isCorrectIntensity());
        T4_TXT_GreyThreshold.setText(settings.getGreyDetectionThreshold() + "");
        T4_TXT_ScaleUp.setText(settings.getScaleUpValue() + "");
        T4_TXT_ScaleDown.setText(settings.getScaleDownValue() + "");

        //Bindings to disable parts of the UI if required.
        T4_TXT_EnhancementValue.disableProperty().bind(T4_CHK_EnhanceColors.selectedProperty().not());
        T4_CHK_EnhancePerChannel.disableProperty().bind(T4_CHK_EnhanceColors.selectedProperty().not());
        T4_TXT_ChannelRed.disableProperty().bind(T4_CHK_EnhanceColors.selectedProperty().not());
        T4_TXT_ChannelGreen.disableProperty().bind(T4_CHK_EnhanceColors.selectedProperty().not());
        T4_TXT_ChannelBlue.disableProperty().bind(T4_CHK_EnhanceColors.selectedProperty().not());

        T4_TXT_GreyThreshold.disableProperty().bind(T4_CHK_CorrectIntensity.selectedProperty().not());
        T4_TXT_ScaleUp.disableProperty().bind(T4_CHK_CorrectIntensity.selectedProperty().not());
        T4_TXT_ScaleDown.disableProperty().bind(T4_CHK_CorrectIntensity.selectedProperty().not());
    }

    @FXML
    void onTabFourSaveClicked(ActionEvent event) {
        try {
            TabFourSaveEvent evt = new TabFourSaveEvent();
            evt.T4_CHK_EnhanceColors = T4_CHK_EnhanceColors;
            evt.T4_TXT_EnhancementValue = T4_TXT_EnhancementValue;
            evt.T4_CHK_EnhancePerChannel = T4_CHK_EnhancePerChannel;
            evt.T4_TXT_ChannelRed = T4_TXT_ChannelRed;
            evt.T4_TXT_ChannelGreen = T4_TXT_ChannelGreen;
            evt.T4_TXT_ChannelBlue = T4_TXT_ChannelBlue;
            evt.T4_CHK_CorrectIntensity = T4_CHK_CorrectIntensity;
            evt.T4_TXT_GreyThreshold = T4_TXT_GreyThreshold;
            evt.T4_TXT_ScaleUp = T4_TXT_ScaleUp;
            evt.T4_TXT_ScaleDown = T4_TXT_ScaleDown;

            evt.setCallback(this::updateTabFour);
            evt.setErrorCallback((errorMessages) -> showErrorMessage(errorMessages[0], errorMessages[1]));

            CommandMapper.getInstance().dispatchEvent(evt);
        } catch (Exception e) {
            showErrorMessage("General error", e.getMessage());
        }
    }

    @FXML
    private CheckBox T5_CHK_AutoConnect;
    @FXML
    private ComboBox<String> T5_CMB_CommChannel;
    @FXML
    private TextField T5_TXT_ClockPin;
    @FXML
    private TextField T5_TXT_DataPin;
    @FXML
    private ComboBox<String> T5_CMB_LedType;

    private void updateTabFive() {
        T5_CMB_CommChannel.setItems(FXCollections.observableList(settings.getPorts()));
        if(settings.getPort() != null) {
            T5_CMB_CommChannel.getSelectionModel().select(settings.getPort());
        }
        T5_CHK_AutoConnect.setSelected(settings.isAutoConnect());

        T5_CMB_LedType.setItems(FXCollections.observableArrayList(Arrays.asList("WS2801", "LPD8806")));
    }

    @FXML
    void onConnectClicked(ActionEvent event) {
        try {
            TabFiveConnectEvent evt = new TabFiveConnectEvent();
            evt.T5_CMB_CommChannel = T5_CMB_CommChannel;

            evt.setErrorCallback((errorMessages) -> showErrorMessage(errorMessages[0], errorMessages[1]));

            CommandMapper.getInstance().dispatchEvent(evt);
        } catch (Exception e) {
            showErrorMessage("General error", e.getMessage());
        }
    }

    @FXML
    void onGenerateCodeClicked(ActionEvent event) {
        try {
            TabFiveConnectEvent evt = new TabFiveConnectEvent();
            evt.T5_CMB_LedType = T5_CMB_LedType;
            evt.T5_TXT_ClockPin = T5_TXT_ClockPin;
            evt.T5_TXT_DataPin = T5_TXT_DataPin;

            evt.setCallback(() -> showMessage("Code copied!", "The generated code has been copied to your clipboard!"));

            CommandMapper.getInstance().dispatchEvent(evt);
        } catch (Exception e) {
            showErrorMessage("General error", e.getMessage());
        }
    }

    @FXML
    void onTabFiveSaveClicked(ActionEvent event) {
        try {
            TabFiveSaveEvent evt = new TabFiveSaveEvent();
            evt.T5_CHK_AutoConnect = T5_CHK_AutoConnect;
            evt.T5_CMB_CommChannel = T5_CMB_CommChannel;

            evt.setCallback(this::updateTabFive);
            evt.setErrorCallback((errorMessages) -> showErrorMessage(errorMessages[0], errorMessages[1]));

            CommandMapper.getInstance().dispatchEvent(evt);
        } catch (Exception e) {
            showErrorMessage("General error", e.getMessage());
        }
    }

    @FXML
    private Label T6_LBL_StatusInfo;

    private void updateTabSix() {
        //Update the colors.
        int[][] colors = model.getCurrentColors();

        for (int i = 0; i < colors.length; i++) {
            int[] rgb = colors[i];
            drawCell(i, cellWidth, cellHeight, new Color((double) rgb[0] / 255, (double) rgb[1] / 255, (double) rgb[2] / 255, 1), T6_LedCanvas);
        }

        //Update text and other debug info.
        T6_LBL_StatusInfo.setText("Jambilight running at " + model.getFramerate() + "FPS - Using " + model.getMemUsed() + "MB RAM out of " + model.getMemTotal() + "MB.");
    }

    @FXML
    @SuppressWarnings("unchecked")
    void onOpenDebugLog(ActionEvent event) {
        StageFactory.StageFactoryResult<LogViewController> result = StageFactory.getInstance().createStage("logView.fxml", "JambiLight log view", new Dimension(1150, 650));
        result.getController().loadLog(LOGGER.getInstance().getCurrentLogFile());
    }

    @FXML
    void onOpenRawInputView(ActionEvent event) {
        eventBus.post(new VisualDebugEvent(true));
    }

    @FXML
    void onOpenWebsiteClicked(ActionEvent event) {
        openWebsite("http://www.jambilight.tv");
    }

    @FXML
    void onOpenLicenseSiteClicked(ActionEvent event) {
        openWebsite("http://creativecommons.org/licenses/by-nc/4.0/legalcode");
    }

    //#########################################
    //### Helper methods.
    //#########################################
    /**
     * This method will look for a view in the stack with the given id.
     * It will then bring the new view to the front (setting its opacity to 1, and the old view's opacity to 0)
     *
     * @param viewId The ID of the view that needs to be brought to the front of the stack.
     */
    private void setActiveStackView(String viewId) {
        ObservableList<Node> children = STCK_TabContainer.getChildrenUnmodifiable();

        for (Node child : children) {
            if(viewId.equals(child.getId())) {
                children.get(0).setOpacity(0);
                child.toFront();
                child.setOpacity(1);
                break;
            }
        }
    }

    private void openWebsite(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            showErrorMessage("Error!", "Cannot open browser to go to website!");
        }
    }

    /**
     * Show an info popup message with the given title and message.
     *
     * @param title The title to display.
     * @param message The message to display.
     */
    private void showMessage(String title, String message) {
        Dialogs.create()
                .owner(STCK_TabContainer)
                .title(title)
                .message(message)
                .lightweight()
                .showInformation();
    }

    /**
     * Show an error popup message with the given title and message.
     *
     * @param title The title to display.
     * @param message The message to display.
     */
    private void showErrorMessage(String title, String message) {
        Dialogs.create()
                .owner(STCK_TabContainer)
                .title(title)
                .message(message)
                .lightweight()
                .showError();
    }

    private void drawDebugUI() {
        cellWidth = T6_LedCanvas.getWidth() / settings.getHorizontalRegions();
        cellHeight = T6_LedCanvas.getHeight() / settings.getVerticalRegions();

        GraphicsContext gc = T6_LedCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, T6_LedCanvas.getWidth(), T6_LedCanvas.getHeight());
        gc.drawImage(image, cellWidth, cellHeight, T6_LedCanvas.getWidth() - cellWidth * 2, T6_LedCanvas.getHeight() - cellHeight * 2);

        int totalRegions = settings.getHorizontalRegions() * 2 + (settings.getVerticalRegions() - 2) * 2;
        for (int i = 0 ; i < totalRegions ; i++) {
            Color c;
            if(i % 2 == 0) {
                c = Color.GREEN;
            } else {
                c = Color.RED;
            }
            drawCell(i, cellWidth, cellHeight, c, T6_LedCanvas);
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
}