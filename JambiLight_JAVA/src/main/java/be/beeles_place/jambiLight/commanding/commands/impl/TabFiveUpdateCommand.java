package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.impl.TabFiveUpdateEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import javafx.collections.FXCollections;

import java.util.Arrays;

public class TabFiveUpdateCommand implements ICommand<TabFiveUpdateEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabFiveUpdateEvent payload) {
        payload.T5_CMB_CommChannel.setItems(FXCollections.observableList(settings.getPorts()));
        if(settings.getPort() != null) {
            payload.T5_CMB_CommChannel.getSelectionModel().select(settings.getPort());
        }
        payload.T5_CHK_AutoConnect.setSelected(settings.isAutoConnect());
        payload.T5_CMB_LedType.setItems(FXCollections.observableArrayList(Arrays.asList("WS2801", "LPD8806")));
    }
}