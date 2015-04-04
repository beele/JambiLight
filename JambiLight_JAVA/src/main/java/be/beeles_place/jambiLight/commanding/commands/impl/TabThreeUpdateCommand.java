package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.impl.TabThreeUpdateEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public class TabThreeUpdateCommand implements ICommand<TabThreeUpdateEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabThreeUpdateEvent payload) {
        payload.T3_SLD_PixelStepSize.setValue(settings.getPixelIteratorStepSize());

        payload.T3_CHK_Weighing.setSelected(settings.isWeighColor());
        payload.T3_SLD_WeighFactor.setValue(settings.getWeighFactor());

        payload.T3_CHK_Interpolation.setSelected(settings.isInterpolated());
        payload.T3_SLD_Interpolation.setValue(settings.getInterpolation());

        //Bindings to disable parts of the UI if required.
        payload.T3_SLD_WeighFactor.disableProperty().bind(payload.T3_CHK_Weighing.selectedProperty().not());
        payload.T3_SLD_Interpolation.disableProperty().bind(payload.T3_CHK_Interpolation.selectedProperty().not());
    }
}