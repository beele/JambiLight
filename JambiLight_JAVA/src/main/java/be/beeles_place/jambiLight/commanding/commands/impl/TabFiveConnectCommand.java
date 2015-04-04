package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.EventbusWrapper;
import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.application.ConnectoArduinoEvent;
import be.beeles_place.jambiLight.commanding.events.impl.TabFiveConnectEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public class TabFiveConnectCommand implements ICommand<TabFiveConnectEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabFiveConnectEvent payload) {
        try {
            if (payload.T5_CMB_CommChannel.getValue() != null && !payload.T5_CMB_CommChannel.getValue().trim().isEmpty()) {
                settings.setPort(payload.T5_CMB_CommChannel.getSelectionModel().getSelectedItem());
                EventbusWrapper.getInstance().post(new ConnectoArduinoEvent());
            } else {
                payload.getErrorCallback().accept(new String[]{"No port selected!", "Please select a comm port from the list before connecting!"});
            }
        } catch (Exception e) {
            if (payload.getErrorCallback() != null) {
                payload.getErrorCallback().accept(new String[]{"Error in command logic!", e.getMessage()});
            }
        }
    }
}