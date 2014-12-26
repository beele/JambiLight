package be.beeles_place.jambiLight.view.JambiUI;

import be.beeles_place.jambiLight.events.SettingsUpdatedEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.EventbusWrapper;
import be.beeles_place.jambiLight.utils.screenCapture.ScreenCapperStrategy;
import com.google.common.eventbus.EventBus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.controlsfx.dialog.Dialogs;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class NewViewController implements Initializable {

    //Constants
    private final String T1 = "T1_Screen";
    private final String T2 = "T2_Function";
    private final String T3 = "T3_Settings";
    private final String T4 = "T4_Advanced";
    private final String T5 = "T5_Arduino";
    private final String T6 = "T6_Debug";
    private final String T7 = "T7_Info";

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

        image = new Image(getClass().getResourceAsStream("/be/beeles_place/jambiLight/view/assets/tv_new.png"));
    }

    public void initUI(ColorModel model, SettingsModel settings) {
        this.settings = settings;
        this.model = model;

        //Activate the first tab!
        setActiveStackView(T1);
        updateTabOne();
    }

    //#########################################
    //### Event handlers.
    //#########################################
    @FXML
    void onSideButtonClicked(ActionEvent event) {
        Button target = (Button) event.getTarget();

        //Figure out which button in the sidebar was clicked and change the stack's first child to the corresponding view.
        if(SIDE_BTN_Screen.equals(target)) {
            setActiveStackView(T1);
            updateTabOne();
        } else if(SIDE_BTN_Function.equals(target)) {
            setActiveStackView(T2);
            updateTabTwo();
        } else if(SIDE_BTN_Settings.equals(target)) {
            setActiveStackView(T3);
            updateTabThree();
        } else if(SIDE_BTN_Advanced.equals(target)) {
            setActiveStackView(T4);
            updateTabFour();
        } else if(SIDE_BTN_Arduino.equals(target)) {
            setActiveStackView(T5);
            updateTabFive();
        } else if(SIDE_BTN_Debug.equals(target)) {
            setActiveStackView(T6);
            drawDebugUI();
        } else if(SIDE_BTN_Info.equals(target)) {
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
        T1_TXT_VerticalLeds.setText(settings.getVerticalRegions() + "");
        T1_TXT_HorizontalLeds.setText(settings.getHorizontalRegions() + "");

        //TODO: Total!

        T1_SLD_VerticalMarg.setValue(settings.getVerticalMargin());
        T1_SLD_HorizontalMarg.setValue(settings.getHorizontalMargin());
    }

    @FXML
    void onTabOneSaveClicked(ActionEvent actionEvent) {
        int verticalLeds = -1;
        int horizontalLeds = -1;
        int totalLeds = -1;

        //TODO: What should be done when there are values already? This will block most of the logic below!

        try {
            String total = T1_TXT_TotalLeds.getText();
            String vertical = T1_TXT_VerticalLeds.getText();
            String horizontal = T1_TXT_HorizontalLeds.getText();

            if(total != null && !total.trim().isEmpty()) {
                totalLeds = Integer.parseInt(total);

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
                    float ratio = model.getRawWidth() / model.getRawHeight();

                    if(ratio == 1f) {
                        horizontalLeds = totalLeds / 2;
                        verticalLeds = horizontalLeds;
                    } else {
                        horizontalLeds = Math.round(ratio * totalLeds);
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

            //TODO: Remove +2!
            int verticalRegions = ((verticalLeds) / 2) + 2;
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
    void onTabTwoSaveClicked(ActionEvent actionEvent) {
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
    }

    @FXML
    void onTabThreeSaveClicked(ActionEvent actionEvent) {
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

        //TODO: Per channel!

        T4_CHK_CorrectIntensity.setSelected(settings.isCorrectIntensity());
        T4_TXT_GreyThreshold.setText(settings.getGreyDetectionThreshold() + "");
        T4_TXT_ScaleUp.setText(settings.getScaleUpValue() + "");
        T4_TXT_ScaleDown.setText(settings.getScaleDownValue() + "");
    }

    @FXML
    void onTabFourSaveClicked(ActionEvent actionEvent) {
        float enhanceValue;
        int gThreshold;
        float scaleUp;
        float scaleDown;

        try {
            //Only update when enabled!
            if(T4_CHK_EnhanceColors.isSelected()) {
                enhanceValue = Float.parseFloat(T4_TXT_EnhancementValue.getText());
                if(enhanceValue < 1f || enhanceValue > 10f) {
                    throw new Exception("Color enhance value should be in range of [1 , 10]");
                }

                //TODO: Per channel!
                settings.setEnhanceValue(enhanceValue);
            }
            settings.setEnhanceColor(T4_CHK_EnhanceColors.isSelected());

            //Only update when enabled!
            if(T4_CHK_CorrectIntensity.isSelected()) {
                gThreshold = Integer.parseInt(T4_TXT_GreyThreshold.getText());
                scaleUp = Float.parseFloat(T4_TXT_ScaleUp.getText());
                scaleDown = Float.parseFloat(T4_TXT_ScaleDown.getText());
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

    private void updateTabFive() {
        T5_CMB_CommChannel.setItems(FXCollections.observableList(settings.getPorts()));
        if(settings.getPort() != null) {
            T5_CMB_CommChannel.getSelectionModel().select(settings.getPort());
        }
        T5_CHK_AutoConnect.setSelected(settings.isAutoConnect());
    }

    @FXML
    void onTabFiveSaveClicked(ActionEvent actionEvent) {
        settings.setPort(T5_CMB_CommChannel.getSelectionModel().getSelectedItem());
        settings.setAutoConnect(T5_CHK_AutoConnect.selectedProperty().getValue());

        //Update the view.
        updateTabFive();
        eventBus.post(new SettingsUpdatedEvent());
    }

    private void updateTabSix() {

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

    /**
     * Update the colors.
     */
    public void updateColors() {
        int[][] colors = model.getCurrentColors();

        for (int i = 0; i < colors.length; i++) {
            int[] rgb = colors[i];
            drawCell(i, cellWidth, cellHeight, new Color((double) rgb[0] / 255, (double) rgb[1] / 255, (double) rgb[2] / 255, 1), T6_LedCanvas);
        }
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