package be.beeles_place.jambiLight.view;

import be.beeles_place.jambiLight.events.ConnectoArduinoEvent;
import be.beeles_place.jambiLight.events.SettingsUpdatedEvent;
import be.beeles_place.jambiLight.events.UpdateUserInterfaceEvent;
import be.beeles_place.jambiLight.events.VisualDebugEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.ArduinoCode;
import be.beeles_place.jambiLight.utils.EventbusWrapper;
import be.beeles_place.jambiLight.utils.StageFactory;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.ScreenCapperStrategy;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

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
        int verticalLeds = -1;
        int horizontalLeds = -1;
        int totalLeds = -1;

        //TODO: What should be done when there are values already? This will block most of the logic below!

        try {
            String total = T1_TXT_TotalLeds.getText();
            String vertical = T1_TXT_VerticalLeds.getText();
            String horizontal = T1_TXT_HorizontalLeds.getText();

            if(total != null && !total.trim().isEmpty()) {
                totalLeds = Integer.parseInt(total) + 4;

                //Check for even number of LEDs
                if(totalLeds % 2 != 0) {
                    throw new Exception("The total number of LEDs should be even!");
                }

                //Check to see if either vertical or horizontal are filled.
                if(vertical != null && !vertical.trim().isEmpty()) {
                    verticalLeds = Integer.parseInt(vertical) * 2;
                    horizontalLeds = totalLeds - verticalLeds;
                }
                if(horizontal != null && !horizontal.trim().isEmpty()) {
                    horizontalLeds = Integer.parseInt(horizontal) * 2;
                    verticalLeds = totalLeds - horizontalLeds;
                }

                if(verticalLeds == -1 && horizontalLeds == -1) {
                    //Calculate the optimal H/V ratio for the LEDs based on total LEDs and screen resolution.
                    double ratio = model.getScreenDimensions().getWidth() / model.getScreenDimensions().getHeight();

                    if(ratio == 1f) {
                        horizontalLeds = totalLeds / 2;
                        verticalLeds = horizontalLeds;
                    } else {
                        horizontalLeds = (int)Math.floor(totalLeds * ratio / (1 + ratio));
                        verticalLeds = totalLeds - horizontalLeds;
                    }
                }

            } else {
                //Check to see if both vertical and horizontal are filled.
                if(vertical != null && !vertical.trim().isEmpty() && horizontal != null && !horizontal.trim().isEmpty()) {
                    verticalLeds = Integer.parseInt(vertical) * 2;
                    horizontalLeds = Integer.parseInt(horizontal) * 2;

                    //Check for even number of LEDs
                    if((verticalLeds + horizontalLeds) % 2 != 0) {
                        throw new Exception("The total number of LEDs should be even!");
                    }

                } else {
                    throw new Exception("Either horizontal and vertical, total, or total and either horizontal and vertical must be filled!");
                }
            }

            //Calculate the region sizes based on the number of LEDs
            //Also correct for problematic uneven LED counts.
            if(verticalLeds % 2 != 0 || horizontalLeds % 2 != 0) {
                verticalLeds += 1;
                horizontalLeds -= 1;
            }

            //TODO: Adjust these numbers! Test what is the actual minimum.
            if(horizontalLeds < 5 || verticalLeds < 5 || totalLeds < 20) {
                throw new Exception("Amount of LEDs is too small. At least 20 in total required!");
            }

            int verticalRegions = ((verticalLeds) / 2);
            int horizontalRegions = (horizontalLeds / 2);

            settings.setVerticalRegions(verticalRegions);
            settings.setHorizontalRegions(horizontalRegions);
            settings.setVerticalMargin((int)T1_SLD_VerticalMarg.getValue());
            settings.setHorizontalMargin((int)T1_SLD_HorizontalMarg.getValue());

            //Notify the application about the updated settings.
            eventBus.post(new SettingsUpdatedEvent());
            //Update the view.
            updateTabOne();

        } catch (Exception e) {
            showErrorMessage("Error saving settings!", e.getMessage());
        }
    }

    @FXML
    private ComboBox<ScreenCapperStrategy> T2_CMB_CaptureMode;

    private void updateTabTwo() {
        T2_CMB_CaptureMode.setItems(FXCollections.observableArrayList(new ArrayList<ScreenCapperStrategy>(Arrays.asList(ScreenCapperStrategy.values()))));
        if(settings.getCaptureMode() != null) {
            T2_CMB_CaptureMode.getSelectionModel().select(settings.getCaptureMode());
        }
    }

    @FXML
    void onTabTwoSaveClicked(ActionEvent event) {
        settings.setCaptureMode(T2_CMB_CaptureMode.getValue());
        //Update the view.
        updateTabTwo();
        eventBus.post(new SettingsUpdatedEvent());
    }

    @FXML
    private Slider T3_SLD_PixelStepSize;
    @FXML
    private CheckBox T3_CHK_Weighing;
    @FXML
    private Slider T3_SLD_WeighFactor;
    
    private void updateTabThree() {
        T3_SLD_PixelStepSize.setValue(settings.getPixelIteratorStepSize());

        T3_CHK_Weighing.setSelected(settings.isWeighColor());
        T3_SLD_WeighFactor.setValue(settings.getWeighFactor());

        //Bindings to disable parts of the UI if required.
        T3_SLD_WeighFactor.disableProperty().bind(T3_CHK_Weighing.selectedProperty().not());
    }

    @FXML
    void onTabThreeSaveClicked(ActionEvent event) {
        int weighFactor = ((int) T3_SLD_WeighFactor.getValue());

        //Only save settings when no errors have occurred!
        settings.setWeighColor(T3_CHK_Weighing.selectedProperty().getValue());
        settings.setWeighFactor(weighFactor);

        //Update the view.
        updateTabThree();
        eventBus.post(new SettingsUpdatedEvent());
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
            //Only update when enabled!
            if(T4_CHK_EnhanceColors.isSelected()) {
                float enhanceValue = Float.parseFloat(T4_TXT_EnhancementValue.getText());

                if(enhanceValue < 1f || enhanceValue > 10f) {
                    throw new Exception("Color enhance value should be in range of [1 , 10]");
                }

                settings.setEnhanceValue(enhanceValue);

                if(T4_CHK_EnhancePerChannel.isSelected()) {
                    float enhanceValueR = Float.parseFloat(T4_TXT_ChannelRed.getText());
                    float enhanceValueG = Float.parseFloat(T4_TXT_ChannelGreen.getText());
                    float enhanceValueB = Float.parseFloat(T4_TXT_ChannelBlue.getText());

                    if(enhanceValueR < 0f || enhanceValueR > 10f) {
                        throw new Exception("Red enhancement value should be in range of [0, 10]");
                    }
                    if(enhanceValueG < 0f || enhanceValueG > 10f) {
                        throw new Exception("Red enhancement value should be in range of [0, 10]");
                    }
                    if(enhanceValueB < 0f || enhanceValueB > 10f) {
                        throw new Exception("Red enhancement value should be in range of [0, 10]");
                    }

                    settings.setEnhanceValueR(enhanceValueR);
                    settings.setEnhanceValueG(enhanceValueG);
                    settings.setEnhanceValueB(enhanceValueB);
                }
            }
            settings.setEnhanceColor(T4_CHK_EnhanceColors.isSelected());
            settings.setEnhancePerChannel(T4_CHK_EnhancePerChannel.isSelected());

            //Only update when enabled!
            if(T4_CHK_CorrectIntensity.isSelected()) {
                int gThreshold = Integer.parseInt(T4_TXT_GreyThreshold.getText());
                float scaleUp = Float.parseFloat(T4_TXT_ScaleUp.getText());
                float scaleDown = Float.parseFloat(T4_TXT_ScaleDown.getText());

                if(gThreshold < 0 || scaleUp < 0 || scaleDown < 0) {
                    throw new Exception("Threshold, scale-up and scale-down should be greater than 0!");
                }

                settings.setGreyDetectionThreshold(gThreshold);
                settings.setScaleUpValue(scaleUp);
                settings.setScaleDownValue(scaleDown);
            }
            settings.setCorrectIntensity(T4_CHK_CorrectIntensity.isSelected());
        } catch (Exception e) {
            showErrorMessage("Cannot save settings!", e.getMessage());
            return;
        }

        //Update the view.
        updateTabFour();
        eventBus.post(new SettingsUpdatedEvent());
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
        eventBus.post(new ConnectoArduinoEvent());
    }

    @FXML
    void onGenerateCodeClicked(ActionEvent event) {
        String clockPin = T5_TXT_ClockPin.getText();
        String dataPin = T5_TXT_DataPin.getText();

        String stripType = T5_CMB_LedType.getSelectionModel().getSelectedItem();

        if(     clockPin != null && !clockPin.trim().isEmpty() &&
                dataPin != null && !dataPin.trim().isEmpty() &&
                stripType != null && !stripType.trim().isEmpty()) {

            //Generate the code and set it on the clipboard!
            String code = ArduinoCode.generateCode(model.getNumberOfConsolidatedRegions(), clockPin, dataPin, stripType.equals("WS2801"));
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(code), null);

            showMessage("Code copied!", "The generated code has been copied to your clipboard!");
        } else {
            showErrorMessage("Error generation code!", "Please fill in clock, data pin numbers and select a LED strip type!");
        }
    }

    @FXML
    void onTabFiveSaveClicked(ActionEvent event) {
        settings.setPort(T5_CMB_CommChannel.getSelectionModel().getSelectedItem());
        settings.setAutoConnect(T5_CHK_AutoConnect.selectedProperty().getValue());

        //Update the view.
        updateTabFive();
        eventBus.post(new SettingsUpdatedEvent());
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