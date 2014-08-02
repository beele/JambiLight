#include "stdafx.h"
#include <stdio.h>
//#include <math.h>
#include "be_beeles_place_jambiLight_utils_screenCapture_impl_ScreenCapperJNIMock.h"

//Globals.
int width = 1920;
int height = 1080;
int pixels = width * height;

//Rainbow globals.
/*int rbCount = 0;
int r = 0;
int g = 0;
int b = 0;
float frequency = 0.3f;*/

//Screencapture globals.
HDC hScreenDC;
HDC hMemoryDC;
HBITMAP hBitmap;
HBITMAP hOldBitmap;
int x;
int y;
BYTE* ScreenData = new BYTE[3*width*height];
BITMAPINFOHEADER bmi;

jint JNI_OnLoad(JavaVM *vm, void *reserved) {

	// get the device context of the screen
	hScreenDC = CreateDC("DISPLAY", NULL, NULL, NULL);     
	// and a device context to put it in
	hMemoryDC = CreateCompatibleDC(hScreenDC);
	x = GetDeviceCaps(hScreenDC, HORZRES);
	y = GetDeviceCaps(hScreenDC, VERTRES);
	// maybe worth checking these are positive values
	hBitmap = CreateCompatibleBitmap(hScreenDC, x, y);

	BITMAPINFOHEADER lbmi = {0};
    lbmi.biSize = sizeof(BITMAPINFOHEADER);
    lbmi.biPlanes = 1;
    lbmi.biBitCount = 24;
	lbmi.biWidth = width;
	lbmi.biHeight = height;
    lbmi.biCompression = BI_RGB;
	lbmi.biSizeImage = pixels;
	bmi = lbmi;

	return JNI_VERSION_1_8;
}

inline int PosR(int x, int y) {
    return ScreenData[3*((y*width)+x)+2];
}

inline int PosG(int x, int y) {
    return ScreenData[3*((y*width)+x)+1];
}

inline int PosB(int x, int y) {
    return ScreenData[3*((y*width)+x)];
}

JNIEXPORT jintArray JNICALL Java_be_beeles_1place_jambiLight_utils_screenCapture_impl_ScreenCapperJNIMock_captureViaJNI(JNIEnv *env, jobject javaObject) {

	// get a new bitmap
	hOldBitmap = (HBITMAP)SelectObject(hMemoryDC, hBitmap);

	BitBlt(hMemoryDC, 0, 0, width, height, hScreenDC, 0, 0, SRCCOPY);
	hBitmap = (HBITMAP)SelectObject(hMemoryDC, hOldBitmap);

	GetDIBits(hScreenDC, hBitmap, 0, height, ScreenData, (BITMAPINFO*)&bmi, DIB_RGB_COLORS);

	jintArray pixelValues = env->NewIntArray(pixels);
	jint *narr = env->GetIntArrayElements(pixelValues, NULL);

	int count = 0;
	for(int i = height-1 ; i >= 0 ; i--) {
		for(int j = 0 ; j < width ; j++) {
			
			narr[count] =  ((PosR(j,i) & 0xff) << 16) + ((PosG(j,i) & 0xff) << 8) + (PosB(j,i) & 0xff);

			count++;
		}
	}

    /*//Rainbows!
    r = (int)(sin(frequency * rbCount + 0) * 127 + 128);
    g = (int)(sin(frequency * rbCount + 2) * 127 + 128);
    b = (int)(sin(frequency * rbCount + 4) * 127 + 128);

	if (rbCount < 32) {
		rbCount++;
	} else {
        rbCount = 0;
	}

	for (int i=0; i < pixels; i++) {
		narr[i] = ((r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff);
	}*/
		
	env->ReleaseIntArrayElements(pixelValues, narr, NULL);
	return pixelValues;
}

void JNI_OnUnload(JavaVM *vm, void *reserved) {
	// clean up
	DeleteDC(hMemoryDC);
	DeleteDC(hScreenDC);
}