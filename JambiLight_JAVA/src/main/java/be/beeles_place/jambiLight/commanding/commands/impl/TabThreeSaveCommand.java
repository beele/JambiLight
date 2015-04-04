package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.EventbusWrapper;
import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.application.SettingsUpdatedEvent;
import be.beeles_place.jambiLight.commanding.events.impl.TabThreeSaveEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public class TabThreeSaveCommand implements ICommand<TabThreeSaveEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabThreeSaveEvent payload) {
        try {
            //Only save settings when no errors have occurred!
            settings.setWeighColor(payload.T3_CHK_Weighing.selectedProperty().getValue());
            settings.setWeighFactor((int) payload.T3_SLD_WeighFactor.getValue());

            settings.setInterpolated(payload.T3_CHK_Interpolation.selectedProperty().getValue());
            settings.setInterpolation((float)payload.T3_SLD_Interpolation.getValue());

            //Update the view.
            EventbusWrapper.getInstance().post(new SettingsUpdatedEvent());
        } catch (Exception e) {
            if(payload.getErrorCallback() != null) {
                payload.getErrorCallback().accept(new String[]{"No port selected!", e.getMessage()});
            }
        }
    }
}