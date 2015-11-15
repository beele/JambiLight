package be.beeles_place.jambiLight.utils;

public class ArduinoCode {

    public static String generateCode(int numberOfLEDs, String clockPin, String dataPin, LedType ledtype) {
        String code = CODE_TEMPLATE;

        switch (ledtype) {
            case LPD8806:
                code = "#include \"LPD8806.h\"\n" + code;
                code = code.replace("$LEDSTRIP$", LPD8806);
                break;
            case WS2801:
                code = "#include \"Adafruit_WS2801.h\"\n" + code;
                code = code.replace("$LEDSTRIP$", WS2801);
                break;
            case NEOPIXEL_V1:
                break;
            case NEOPIXEL_V2:
                break;
        }

        code = code.replace("$AMOUNTOFLEDS$", numberOfLEDs + "").replace("$CLOCK$", clockPin).replace("$DATA$", dataPin);

        return code;
    }

    private static final String WS2801 =
            "/** ========================================\n" +
            "**  Init block for WS2801 strips \n" +
            "**  ========================================**/\n" +
            "int rgbDivFactor = 1;\n" +
            "Adafruit_WS2801 strip = Adafruit_WS2801(nLEDs, dataPin, clockPin);\n";

    private static final String LPD8806 =
            "/** ========================================\n" +
            "**  Init block for LPD8806 strips \n" +
            "**  ========================================**/\n" +
            "int rgbDivFactor = 2;\n" +
            "LPD8806 strip = LPD8806(nLEDs, dataPin, clockPin);\n";

    private static final String CODE_TEMPLATE =
            "#include \"SPI.h\"\n" +
            "//Created by Kevin Van den Abeele\n" +
            "//Free to use and reproduce.\n" +
            "\n" +
            "//Number of RGB LEDs in strand:\n" +
            "int const nLEDs = $AMOUNTOFLEDS$;\n" +
            "\n" +
            "//Clock and data pin.\n" +
            "int dataPin = $DATA$;\n" +
            "int clockPin = $CLOCK$;\n" +
            "\n" +
            "$LEDSTRIP$\n" +
            "\n" +
            "void setup() {\n" +
            "    //Start serial communication.\n" +
            "    Serial.begin(100000);\n" +
            "  \n" +
            "    //Start up the LED strip\n" +
            "    strip.begin();\n" +
            "    //Make sure all LEDs are off.\n" +
            "    for(int i = 0 ; i < nLEDs ; i++) {\n" +
            "        strip.setPixelColor(i,0);  \n" +
            "    }\n" +
            "    //Update the strip\n" +
            "    strip.show();\n" +
            "}\n" +
            "\n" +
            "//Variables required in the loop and serial logic.\n" +
            "int const totalBytes = nLEDs * 3;\n" +
            "int const stepSize = 48;\n" +
            "int colors[totalBytes];\n" +
            "int bytesSaved = 0;\n" +
            "int stepCounter = 0;\n" +
            "\n" +
            "void loop() {\n" +
            "    //Only \"stepSize\" bytes are saved at once. (Because the serial buffer is only 64 bytes long)\n" +
            "    //The client software will only send \"stepSize\" bytes at a time and then wait for the go ahead signal [Serial.write(50)] until sending the next \"stepSize\" bytes. \n" +
            "    if (Serial.available() == stepSize) {\n" +
            "        for(int i = 0 ; i < stepSize ; i++) {\n" +
            "            int index = ((stepSize * stepCounter) + i);\n" +
            "\n" +
            "            //Make sure no illegal array access occurs. More data will be sent then is actually needed (sometimes)\n" +
            "            if(index < totalBytes) {\n" +
            "                colors[index] = Serial.read();   \n" +
            "            } else {\n" +
            "                Serial.read(); \n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        bytesSaved += stepSize;\n" +
            "        stepCounter += 1;\n" +
            "        Serial.write(50);\n" +
            "    }\n" +
            "\n" +
            "    //If all the bytes have been received, process them\n" +
            "    if(bytesSaved >= totalBytes) {\n" +
            "        for(int i = 0 ; i < nLEDs ; i++) {\n" +
            "            //Get the R/G/B values. The rgbDivFactor is used to weigh the colors correctly depending of the type of LED strip!\n" +
            "            int rVal = colors[i * 3] / rgbDivFactor; \n" +
            "            int gVal = colors[(i * 3) + 1] / rgbDivFactor;\n" +
            "            int bVal = colors[(i * 3) + 2] / rgbDivFactor;\n" +
            "\n" +
            "            //Set each pixel. \n" +
            "            strip.setPixelColor(i,rVal,gVal,bVal); \n" +
            "        }\n" +
            "\n" +
            "        //Update the strip by showing all the new colors.\n" +
            "        strip.show(); \n" +
            "        //Reset counter variables so the next colors can be sent.\n" +
            "        bytesSaved = 0;\n" +
            "        stepCounter = 0; \n" +
            "    }\n" +
            "    delay(5);\n" +
            "}\n";
}
