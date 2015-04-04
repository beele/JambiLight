package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.impl.TabFourUpdateEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public class TabFourUpdateCommand implements ICommand<TabFourUpdateEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabFourUpdateEvent payload) {
        payload.T4_CHK_EnhanceColors.setSelected(settings.isEnhanceColor());
        payload.T4_TXT_EnhancementValue.setText(settings.getEnhanceValue() + "");

        payload.T4_CHK_EnhancePerChannel.setSelected(settings.isEnhancePerChannel());
        payload.T4_TXT_ChannelRed.setText(settings.getEnhanceValueR() + "");
        payload.T4_TXT_ChannelGreen.setText(settings.getEnhanceValueG() + "");
        payload.T4_TXT_ChannelBlue.setText(settings.getEnhanceValueB() + "");

        payload.T4_CHK_CorrectIntensity.setSelected(settings.isCorrectIntensity());
        payload.T4_TXT_GreyThreshold.setText(settings.getGreyDetectionThreshold() + "");
        payload.T4_TXT_ScaleUp.setText(settings.getScaleUpValue() + "");
        payload.T4_TXT_ScaleDown.setText(settings.getScaleDownValue() + "");

        //Bindings to disable parts of the UI if required.
        payload.T4_TXT_EnhancementValue.disableProperty().bind(payload.T4_CHK_EnhanceColors.selectedProperty().not());
        payload.T4_CHK_EnhancePerChannel.disableProperty().bind(payload.T4_CHK_EnhanceColors.selectedProperty().not());
        payload.T4_TXT_ChannelRed.disableProperty().bind(payload.T4_CHK_EnhanceColors.selectedProperty().not());
        payload.T4_TXT_ChannelGreen.disableProperty().bind(payload.T4_CHK_EnhanceColors.selectedProperty().not());
        payload.T4_TXT_ChannelBlue.disableProperty().bind(payload.T4_CHK_EnhanceColors.selectedProperty().not());

        payload.T4_TXT_GreyThreshold.disableProperty().bind(payload.T4_CHK_CorrectIntensity.selectedProperty().not());
        payload.T4_TXT_ScaleUp.disableProperty().bind(payload.T4_CHK_CorrectIntensity.selectedProperty().not());
        payload.T4_TXT_ScaleDown.disableProperty().bind(payload.T4_CHK_CorrectIntensity.selectedProperty().not());
    }
}