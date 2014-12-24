package be.beeles_place.jambiLight.view.JambiUI;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.Serializable;

public class NewViewController implements Serializable {

    private final String T1 = "T1_Screen";
    private final String T2 = "T2_Function";
    private final String T3 = "T3_Settings";
    private final String T4 = "T4_Advanced";
    private final String T5 = "T5_Arduino";
    private final String T6 = "T6_Debug";
    private final String T7 = "T7_Info";

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

    //Event handlers:
    @FXML
    void onSideButtonClicked(ActionEvent event) {
        Button target = (Button) event.getTarget();

        //Figure out which button in the sidebar was clicked and change the stack's first child to the corresponding view.
        if(SIDE_BTN_Screen.equals(target)) {
            setActiveStackView(T1);
        } else if(SIDE_BTN_Function.equals(target)) {
            setActiveStackView(T2);
        } else if(SIDE_BTN_Settings.equals(target)) {
            setActiveStackView(T3);
        } else if(SIDE_BTN_Advanced.equals(target)) {
            setActiveStackView(T4);
        } else if(SIDE_BTN_Arduino.equals(target)) {
            setActiveStackView(T5);
        } else if(SIDE_BTN_Debug.equals(target)) {
            setActiveStackView(T6);
        } else if(SIDE_BTN_Info.equals(target)) {
            setActiveStackView(T7);
        }
    }

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
}