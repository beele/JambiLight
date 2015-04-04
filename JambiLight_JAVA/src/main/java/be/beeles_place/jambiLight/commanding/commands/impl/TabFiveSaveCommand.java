package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.EventbusWrapper;
import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.application.SettingsUpdatedEvent;
import be.beeles_place.jambiLight.commanding.events.impl.TabFiveSaveEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public class TabFiveSaveCommand implements ICommand<TabFiveSaveEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabFiveSaveEvent payload) {
        try {
            settings.setPort(payload.T5_CMB_CommChannel.getSelectionModel().getSelectedItem());
            settings.setAutoConnect(payload.T5_CHK_AutoConnect.selectedProperty().getValue());

            //Update the view.
            EventbusWrapper.getInstance().post(new SettingsUpdatedEvent());
        } catch (Exception e) {
            if(payload.getErrorCallback() != null) {
                payload.getErrorCallback().accept(new String[]{"Error saving settings!", e.getMessage()});
            }
        }
    }
}