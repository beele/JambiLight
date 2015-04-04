package be.beeles_place.jambiLight.view;

import be.beeles_place.jambiLight.commanding.CommandMapper;
import be.beeles_place.jambiLight.commanding.events.impl.*;
import be.beeles_place.jambiLight.commanding.events.application.UpdateUserInterfaceEvent;
import be.beeles_place.jambiLight.commanding.events.application.VisualDebugEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.commanding.EventbusWrapper;
import be.beeles_place.jambiLight.utils.StageFactory;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class MainViewController implements Initializable {

    //Constants
    private final String T1 = "T1_Screen";
    private final String T2 = "T2_Function";
    private final String T3 = "T3_Settings";
    private final String T4 = "T4_Advanced";
    private final String T5 = "T5_Arduino";
    private final String T6 = "T6_Debug";
    private final String T7 = "T7_Info";

    //Stage
    private Stage stage;

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

    //Local variables
    private Image image;

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

    //#########################################
    //### Tab actions and handlers.
    //#########################################
    private void updateTabOne() {
        CommandMapper.getInstance().dispatchEvent(new TabOneUpdateEvent(stage));
    }

    @FXML
    void onTabOneSaveClicked(ActionEvent event) {
        TabOneSaveEvent evt = new TabOneSaveEvent(stage);
        evt.setCallback(this::updateTabOne);
        evt.setErrorCallback((errorMessages) -> showErrorMessage(errorMessages[0], errorMessages[1]));
        CommandMapper.getInstance().dispatchEvent(evt);
    }

    private void updateTabTwo() {
        CommandMapper.getInstance().dispatchEvent(new TabTwoUpdateEvent(stage));
    }

    @FXML
    void onTabTwoSaveClicked(ActionEvent event) {
        TabTwoSaveEvent evt = new TabTwoSaveEvent(stage);
        evt.setCallback(this::updateTabTwo);
        evt.setErrorCallback((errorMessages) -> showErrorMessage(errorMessages[0], errorMessages[1]));
        CommandMapper.getInstance().dispatchEvent(evt);
    }
    
    private void updateTabThree() {
        CommandMapper.getInstance().dispatchEvent(new TabThreeUpdateEvent(stage));
    }

    @FXML
    void onTabThreeSaveClicked(ActionEvent event) {
        TabThreeSaveEvent evt = new TabThreeSaveEvent(stage);
        evt.setCallback(this::updateTabThree);
        evt.setErrorCallback((errorMessages) -> showErrorMessage(errorMessages[0], errorMessages[1]));
        CommandMapper.getInstance().dispatchEvent(evt);
    }

    private void updateTabFour() {
        CommandMapper.getInstance().dispatchEvent(new TabFourUpdateEvent(stage));
    }

    @FXML
    void onTabFourSaveClicked(ActionEvent event) {
        TabFourSaveEvent evt = new TabFourSaveEvent(stage);
        evt.setCallback(this::updateTabFour);
        evt.setErrorCallback((errorMessages) -> showErrorMessage(errorMessages[0], errorMessages[1]));
        CommandMapper.getInstance().dispatchEvent(evt);
    }

    private void updateTabFive() {
        CommandMapper.getInstance().dispatchEvent(new TabFiveUpdateEvent(stage));
    }

    @FXML
    void onConnectClicked(ActionEvent event) {
        TabFiveConnectEvent evt = new TabFiveConnectEvent(stage);
        evt.setErrorCallback((errorMessages) -> showErrorMessage(errorMessages[0], errorMessages[1]));
        CommandMapper.getInstance().dispatchEvent(evt);
    }

    @FXML
    void onGenerateCodeClicked(ActionEvent event) {
        TabFiveConnectEvent evt = new TabFiveConnectEvent(stage);
        evt.setCallback(() -> showMessage("Code copied!", "The generated code has been copied to your clipboard!"));
        CommandMapper.getInstance().dispatchEvent(evt);
    }

    @FXML
    void onTabFiveSaveClicked(ActionEvent event) {
        TabFiveSaveEvent evt = new TabFiveSaveEvent(stage);
        evt.setCallback(this::updateTabFive);
        evt.setErrorCallback((errorMessages) -> showErrorMessage(errorMessages[0], errorMessages[1]));
        CommandMapper.getInstance().dispatchEvent(evt);
    }

    private void updateTabSix() {
        CommandMapper.getInstance().dispatchEvent(new TabSixUpdateEvent(stage, image));
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
        int totalRegions = settings.getHorizontalRegions() * 2 + (settings.getVerticalRegions() - 2) * 2;
        int[][] debugColors = new int[totalRegions][3];

        for (int i = 0 ; i < totalRegions ; i++) {
            if(i % 2 == 0) {
                debugColors[i][0] = 0;
                debugColors[i][1] = 255;
            } else {
                debugColors[i][0] = 255;
                debugColors[i][1] = 0;
            }
            debugColors[i][2] = 0;
        }

        model.setCurrentColors(debugColors);
        CommandMapper.getInstance().dispatchEvent(new TabSixUpdateEvent(stage, image));
    }

    //Getters & setters:
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}