#include <Adafruit_NeoPixel.h>
#include <Adafruit_WS2801.h>
#include <LPD8806.h>
#include <SPI.h>
//Created by Kevin Van den Abeele
//Consult LICENSE.md for more information.

//Number of RGB LEDs in strand:
int const nLEDs = 64;

//Clock and data pin.
int dataPin = 2;
int clockPin = 3;

//Uncomment the correct block for your type of LED strip.
/** ========================================
**  Init block for LPD8806 strips 
**  ========================================**/
int rgbDivFactor = 2;
LPD8806 strip = LPD8806(nLEDs, dataPin, clockPin);

/** ========================================
**  Init block for WS2801 strips 
**  ========================================**/
/*int rgbDivFactor = 1;
Adafruit_WS2801 strip = Adafruit_WS2801(nLEDs, dataPin, clockPin);*/

/** ========================================
**  Init block for NEOPIXEL strips 
**  ========================================**/
/*int rgbDivFactor = 1;
//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
Adafruit_NeoPixel strip = Adafruit_NeoPixel(nLEDs, dataPin, NEO_GRB + NEO_KHZ800);*/

void setup() {
    //Start serial communication.
    Serial.begin(100000);
  
    //Start up the LED strip
    strip.begin();
    //Make sure all LEDs are off.
    for(int i = 0 ; i < nLEDs ; i++) {
        strip.setPixelColor(i,0);  
    }
    //Update the strip
    strip.show();
}

//Variables required in the loop and serial logic.
int const totalBytes = nLEDs * 3;
int const stepSize = 48;
int colors[totalBytes];
int bytesSaved = 0;
int stepCounter = 0;

void loop() {
    //Only "stepSize" bytes are saved at once. (Because the serial buffer is only 64 bytes long)
    //The client software will only send "stepSize" bytes at a time and then wait for the go ahead signal [Serial.write(50)] until sending the next "stepSize" bytes. 
    if (Serial.available() == stepSize) {
        for(int i = 0 ; i < stepSize ; i++) {
            int index = ((stepSize * stepCounter) + i);

            //Make sure no illegal array access occurs. More data will be sent then is actually needed (sometimes)
            if(index < totalBytes) {
                colors[index] = Serial.read();   
            } else {
                Serial.read(); 
            }
        }

        bytesSaved += stepSize;
        stepCounter += 1;
        Serial.write(50);
    }

    //If all the bytes have been received, process them
    if(bytesSaved >= totalBytes) {
        for(int i = 0 ; i < nLEDs ; i++) {
            //Get the R/G/B values. The rgbDivFactor is used to weigh the colors correctly depending of the type of LED strip!
            int rVal = colors[i * 3] / rgbDivFactor; 
            int gVal = colors[(i * 3) + 1] / rgbDivFactor;
            int bVal = colors[(i * 3) + 2] / rgbDivFactor;

            //Set each pixel. 
            strip.setPixelColor(i,rVal,gVal,bVal); 
        }

        //Update the strip by showing all the new colors.
        strip.show(); 
        //Reset counter variables so the next colors can be sent.
        bytesSaved = 0;
        stepCounter = 0; 
    }
    delay(5);
}
