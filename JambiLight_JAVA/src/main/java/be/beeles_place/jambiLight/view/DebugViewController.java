package be.beeles_place.jambiLight.view;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class DebugViewController {

    @FXML
    private Canvas debugCanvas;

    public void paint(int[] rawImageData, int width, int heigth) {
        GraphicsContext gc = debugCanvas.getGraphicsContext2D();

        int[] rgb = new int[3];
        int tempPixelValue;
        int currentPixel = 0;

        for(int i = 0; i < heigth; i++){
            for(int j = 0; j < width; j++) {
                tempPixelValue = rawImageData[currentPixel++];

                rgb[0] += (tempPixelValue >>> 16) & 0xFF;
                rgb[1] += (tempPixelValue >>> 8) & 0xFF;
                rgb[2] += tempPixelValue & 0xFF;

                gc.setFill(new Color((double) rgb[0] / 255, (double) rgb[1] / 255, (double) rgb[2] / 255, 1));
                gc.fillRect(j, i, 1, 1);
            }
        }
    }
}
