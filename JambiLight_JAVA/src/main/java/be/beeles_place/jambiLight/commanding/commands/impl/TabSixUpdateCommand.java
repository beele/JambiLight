package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.impl.TabSixUpdateEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TabSixUpdateCommand implements ICommand<TabSixUpdateEvent> {

    private SettingsModel settings;

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabSixUpdateEvent payload) {
        this.settings = settings;

        double cellWidth = payload.T6_LedCanvas.getWidth() / settings.getHorizontalRegions();
        double cellHeight = payload.T6_LedCanvas.getHeight() / settings.getVerticalRegions();

        GraphicsContext gc = payload.T6_LedCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, payload.T6_LedCanvas.getWidth(), payload.T6_LedCanvas.getHeight());
        gc.drawImage(payload.image, cellWidth, cellHeight, payload.T6_LedCanvas.getWidth() - cellWidth * 2, payload.T6_LedCanvas.getHeight() - cellHeight * 2);

        //Update the colors.
        int[][] colors = model.getCurrentColors();
        for (int i = 0; i < colors.length; i++) {
            int[] rgb = colors[i];
            drawCell(i, cellWidth, cellHeight, new Color((double) rgb[0] / 255, (double) rgb[1] / 255, (double) rgb[2] / 255, 1), payload.T6_LedCanvas);
        }

        //Update text and other debug info.
        payload.T6_LBL_StatusInfo.setText("Jambilight running at " + model.getFramerate() + "FPS - Using " + model.getMemUsed() + "MB RAM out of " + model.getMemTotal() + "MB.");
    }

    private void drawCell(int number, double cellWidth, double cellHeight, Color color, Canvas canvas) {
        int x = (int)(canvas.getWidth() / 2 - cellWidth / 2);
        int y = (int)(canvas.getHeight() / 2 - cellHeight / 2);

        int h1 = settings.getHorizontalRegions();
        int v1 = h1 + settings.getVerticalRegions() - 1;
        int h2 = v1 + settings.getHorizontalRegions() - 2;
        int v2 = h2 + settings.getVerticalRegions();

        if(number < h1) {
            x = (int)(number * cellWidth);
            y = 0;
        } else if(number >= h1 && number < v1) {
            x = (int)(canvas.getWidth() - cellWidth);
            y = (int)((number - h1 + 1) * cellHeight);
        } else if(number >= v1 && number < h2) {
            x = (int)(canvas.getWidth() - cellWidth * (number - v1 + 2));
            y = (int)(canvas.getHeight() - cellHeight);
        } else if(number >= h2 && number < v2) {
            x = 0;
            y = (int)(canvas.getHeight() - cellHeight * (number - h2 + 1));
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(x, y, cellWidth, cellHeight);
    }
}