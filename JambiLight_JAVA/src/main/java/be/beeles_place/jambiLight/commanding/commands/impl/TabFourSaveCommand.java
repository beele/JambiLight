package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.EventbusWrapper;
import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.application.SettingsUpdatedEvent;
import be.beeles_place.jambiLight.commanding.events.impl.TabFourSaveEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public class TabFourSaveCommand implements ICommand<TabFourSaveEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabFourSaveEvent payload) {
        try {
            //Only update when enabled!
            if(payload.T4_CHK_EnhanceColors.isSelected()) {
                float enhanceValue = Float.parseFloat(payload.T4_TXT_EnhancementValue.getText());

                if(enhanceValue < 1f || enhanceValue > 10f) {
                    throw new Exception("Color enhance value should be in range of [1 , 10]");
                }

                settings.setEnhanceValue(enhanceValue);

                if(payload.T4_CHK_EnhancePerChannel.isSelected()) {
                    float enhanceValueR = Float.parseFloat(payload.T4_TXT_ChannelRed.getText());
                    float enhanceValueG = Float.parseFloat(payload.T4_TXT_ChannelGreen.getText());
                    float enhanceValueB = Float.parseFloat(payload.T4_TXT_ChannelBlue.getText());

                    if(enhanceValueR < 0f || enhanceValueR > 10f) {
                        throw new Exception("Red enhancement value should be in range of [0, 10]");
                    }
                    if(enhanceValueG < 0f || enhanceValueG > 10f) {
                        throw new Exception("Red enhancement value should be in range of [0, 10]");
                    }
                    if(enhanceValueB < 0f || enhanceValueB > 10f) {
                        throw new Exception("Red enhancement value should be in range of [0, 10]");
                    }

                    settings.setEnhanceValueR(enhanceValueR);
                    settings.setEnhanceValueG(enhanceValueG);
                    settings.setEnhanceValueB(enhanceValueB);
                }
            }
            settings.setEnhanceColor(payload.T4_CHK_EnhanceColors.isSelected());
            settings.setEnhancePerChannel(payload.T4_CHK_EnhancePerChannel.isSelected());

            //Only update when enabled!
            if(payload.T4_CHK_CorrectIntensity.isSelected()) {
                int gThreshold = Integer.parseInt(payload.T4_TXT_GreyThreshold.getText());
                float scaleUp = Float.parseFloat(payload.T4_TXT_ScaleUp.getText());
                float scaleDown = Float.parseFloat(payload.T4_TXT_ScaleDown.getText());

                if(gThreshold < 0 || scaleUp < 0 || scaleDown < 0) {
                    throw new Exception("Threshold, scale-up and scale-down should be greater than 0!");
                }

                settings.setGreyDetectionThreshold(gThreshold);
                settings.setScaleUpValue(scaleUp);
                settings.setScaleDownValue(scaleDown);
            }
            settings.setCorrectIntensity(payload.T4_CHK_CorrectIntensity.isSelected());

            //Update the view.
            EventbusWrapper.getInstance().post(new SettingsUpdatedEvent());
        } catch (Exception e) {
            if(payload.getErrorCallback() != null) {
                payload.getErrorCallback().accept(new String[]{"Error saving settings!", e.getMessage()});
            }
        }
    }
}