package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.impl.TabFiveGenerateCodeEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.ArduinoCode;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class TabFiveGenerateCodeCommand implements ICommand<TabFiveGenerateCodeEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabFiveGenerateCodeEvent payload) {
        try {
            String clockPin = payload.T5_TXT_ClockPin.getText();
            String dataPin = payload.T5_TXT_DataPin.getText();

            String stripType = payload.T5_CMB_LedType.getSelectionModel().getSelectedItem();

            if(     clockPin != null && !clockPin.trim().isEmpty() &&
                    dataPin != null && !dataPin.trim().isEmpty() &&
                    stripType != null && !stripType.trim().isEmpty()) {

                //Generate the code and set it on the clipboard!
                String code = ArduinoCode.generateCode(model.getNumberOfConsolidatedRegions(), clockPin, dataPin, stripType.equals("WS2801"));
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(code), null);
            } else {
                payload.getErrorCallback().accept(new String[]{"Error generation code!", "Please fill in clock, data pin numbers and select a LED strip type!"});
            }
        } catch (Exception e) {
            if (payload.getErrorCallback() != null) {
                payload.getErrorCallback().accept(new String[]{"Error in command logic!", e.getMessage()});
            }
        }
    }
}