package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.impl.TabTwoUpdateEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.DirectShowEnumerator;
import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.ScreenCapperStrategy;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TabTwoUpdateCommand implements ICommand<TabTwoUpdateEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabTwoUpdateEvent payload) {
        payload.T2_CMB_CaptureMode.setItems(FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(ScreenCapperStrategy.values()))));
        if(settings.getCaptureMode() != null) {
            payload.T2_CMB_CaptureMode.getSelectionModel().select(settings.getCaptureMode());
        }

        //Bindings to disable parts of the UI if required.
        payload.T2_CMB_DirectShowDevices.disableProperty().bind(payload.T2_CMB_CaptureMode.getSelectionModel().selectedItemProperty().isNotEqualTo(ScreenCapperStrategy.DIRECT_SHOW));

        //Get devices and make them into a list.
        List<String> devices = new ArrayList<>();
        devices.addAll(DirectShowEnumerator.enumerateDirectShowDevices().values().stream().collect(Collectors.toList()));

        payload.T2_CMB_DirectShowDevices.setItems(FXCollections.observableArrayList(devices));
        if(settings.getDirectShowDeviceName() != null) {
            payload.T2_CMB_DirectShowDevices.getSelectionModel().select(settings.getDirectShowDeviceName());
        }
    }
}