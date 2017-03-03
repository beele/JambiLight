package be.beeles_place.jambiLight.view;

import be.beeles_place.jambiLight.model.ColorModel;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.nio.ByteBuffer;

public class DebugViewController {

    private Stage stage;
    private ColorModel model;

    private int currentPixel;
    private int tempPixelValue;
    private int[] rgb;

    private GraphicsContext gc;
    private WritableImage img;
    private PixelWriter writer;

    @FXML
    private Pane pane;
    @FXML
    private Canvas debugCanvas;

    private int width;
    private int heigth;

    public void init(Stage stage, ColorModel model) {
        this.stage = stage;
        this.model = model;

        currentPixel = 0;
        tempPixelValue = 0;
        rgb = new int[3];

        gc = debugCanvas.getGraphicsContext2D();
    }

    public void paint() {
        currentPixel = 0;
        tempPixelValue = 0;

        int[] data = {};
        byte[] dataBytes = {};
        Object rawData = model.getRawImageData();

        if(rawData instanceof int[]) {
            data = (int[])rawData;
        } else {
            dataBytes = (byte[])rawData;
        }

        //Create a new Writable image if required, otherwise reuse it.
        if(model.getRawWidth() != width || model.getRawHeight() != heigth) {
            width = model.getRawWidth();
            heigth = model.getRawHeight();

            debugCanvas.setWidth((double) width);
            debugCanvas.setHeight((double) heigth);
            pane.setMaxWidth((double) width);
            pane.setMaxHeight((double) heigth);
            stage.setTitle("Visual debug view => " + width + " x " + heigth);
            stage.sizeToScene();

            img = new WritableImage(width,heigth);
            writer = img.getPixelWriter();
        }

        if(data.length > 0) {
            //Convert raw data to an image we can draw to the canvas.
            for(int i = 0; i < heigth; i++){
                for(int j = 0; j < width; j++) {
                    tempPixelValue = data[currentPixel++];

                    rgb[0] = (tempPixelValue >>> 16) & 0xFF;
                    rgb[1] = (tempPixelValue >>> 8) & 0xFF;
                    rgb[2] = tempPixelValue & 0xFF;

                    writer.setColor(j, i ,new Color((double) rgb[0] / 255, (double) rgb[1] / 255, (double) rgb[2] / 255, 1));
                }
            }
        } else if(dataBytes.length > 0) {
            //Switch the R and B channels!
            byte[] colors = new byte[dataBytes.length];
            for (int i = 0; i < dataBytes.length; i += 3) {
                colors[i] = dataBytes[i + 2];
                colors[i + 1] = dataBytes[i + 1];
                colors[i + 2] = dataBytes[i];
            }
            PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();
            writer.setPixels(0, 0, width, heigth, pixelFormat, colors, 0, width * 3);
        }
        gc.drawImage(img,0,0);
    }
}