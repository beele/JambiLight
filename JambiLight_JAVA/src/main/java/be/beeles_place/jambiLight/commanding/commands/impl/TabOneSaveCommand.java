package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.EventbusWrapper;
import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.application.SettingsUpdatedEvent;
import be.beeles_place.jambiLight.commanding.events.impl.TabOneSaveEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public class TabOneSaveCommand implements ICommand<TabOneSaveEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabOneSaveEvent payload) {
        int verticalLeds = -1;
        int horizontalLeds = -1;
        int totalLeds = -1;

        //TODO: What should be done when there are values already? This will block most of the logic below!

        try {
            String total = payload.T1_TXT_TotalLeds.getText();
            String vertical = payload.T1_TXT_VerticalLeds.getText();
            String horizontal = payload.T1_TXT_HorizontalLeds.getText();

            if (total != null && !total.trim().isEmpty()) {
                totalLeds = Integer.parseInt(total) + 4;

                //Check for even number of LEDs
                if (totalLeds % 2 != 0) {
                    throw new Exception("The total number of LEDs should be even!");
                }

                //Check to see if either vertical or horizontal are filled.
                if (vertical != null && !vertical.trim().isEmpty()) {
                    verticalLeds = Integer.parseInt(vertical) * 2 + 4;
                    horizontalLeds = totalLeds - verticalLeds;
                }
                if (horizontal != null && !horizontal.trim().isEmpty()) {
                    horizontalLeds = Integer.parseInt(horizontal) * 2;
                    verticalLeds = totalLeds - horizontalLeds;
                }

                if (verticalLeds == -1 && horizontalLeds == -1) {
                    //Calculate the optimal H/V ratio for the LEDs based on total LEDs and screen resolution.
                    double ratio = model.getScreenDimensions().getWidth() / model.getScreenDimensions().getHeight();

                    if (ratio == 1f) {
                        horizontalLeds = totalLeds / 2;
                        verticalLeds = horizontalLeds;
                    } else {
                        horizontalLeds = (int) Math.floor(totalLeds * ratio / (1 + ratio));
                        verticalLeds = totalLeds - horizontalLeds;
                    }
                }

            } else {
                //Check to see if both vertical and horizontal are filled.
                if (vertical != null && !vertical.trim().isEmpty() && horizontal != null && !horizontal.trim().isEmpty()) {
                    verticalLeds = Integer.parseInt(vertical) * 2 + 4;
                    horizontalLeds = Integer.parseInt(horizontal) * 2;
                    totalLeds = verticalLeds + horizontalLeds;

                    //Check for even number of LEDs
                    if ((verticalLeds + horizontalLeds) % 2 != 0) {
                        throw new Exception("The total number of LEDs should be even!");
                    }

                } else {
                    throw new Exception("Either horizontal and vertical, total, or total and either horizontal and vertical must be filled!");
                }
            }

            //Calculate the region sizes based on the number of LEDs
            //Also correct for problematic uneven LED counts.
            if (verticalLeds % 2 != 0 || horizontalLeds % 2 != 0) {
                verticalLeds += 1;
                horizontalLeds -= 1;
            }

            //TODO: Adjust these numbers! Test what is the actual minimum.
            if (horizontalLeds < 5 || verticalLeds < 5 || totalLeds < 20) {
                throw new Exception("Amount of LEDs is too small. At least 20 in total required!");
            }

            int verticalRegions = ((verticalLeds) / 2);
            int horizontalRegions = (horizontalLeds / 2);

            settings.setVerticalRegions(verticalRegions);
            settings.setHorizontalRegions(horizontalRegions);
            settings.setVerticalMargin((int) payload.T1_SLD_VerticalMarg.getValue());
            settings.setHorizontalMargin((int) payload.T1_SLD_HorizontalMarg.getValue());

            //Notify the application about the updated settings.
            EventbusWrapper.getInstance().post(new SettingsUpdatedEvent());

        } catch (Exception e) {
            if(payload.getErrorCallback() != null) {
                payload.getErrorCallback().accept(new String[]{"No port selected!", e.getMessage()});
            }
        }
    }
}