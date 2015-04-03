package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.EventbusWrapper;
import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.SettingsUpdatedEvent;
import be.beeles_place.jambiLight.commanding.events.impl.TabTwoSaveEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public class TabTwoSaveCommand implements ICommand<TabTwoSaveEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabTwoSaveEvent payload) {
        try {
            settings.setCaptureMode(payload.T2_CMB_CaptureMode.getValue());

            if(!payload.T2_CMB_DirectShowDevices.disabledProperty().getValue()) {
                String device = payload.T2_CMB_DirectShowDevices.getValue();
                if(device != null && !device.trim().isEmpty()) {
                    settings.setDirectShowDeviceName(payload.T2_CMB_DirectShowDevices.getValue());
                } else {
                    throw new Exception("Please select a device or a different capture mode!");
                }
            }

            EventbusWrapper.getInstance().post(new SettingsUpdatedEvent());
        } catch (Exception e) {
            if(payload.getErrorCallback() != null) {
                payload.getErrorCallback().accept(e.getMessage());
            }
        }
    }
}