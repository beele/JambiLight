#include "stdafx.h"
#include <stdio.h>
#include <math.h>
#include "be_beeles_place_jambiLight_utils_screenCapture_impl_ScreenCapperJNIMock.h"

int rbCount = 0;
int r = 0;
int g = 0;
int b = 0;

float frequency = 0.3f;

/*BOOL WINAPI DllMain(HANDLE hHandle, DWORD dwReason, LPVOID lpReserved){
	return TRUE;
}*/

JNIEXPORT jintArray JNICALL Java_be_beeles_1place_jambiLight_utils_screenCapture_impl_ScreenCapperJNIMock_captureViaJNI(JNIEnv *env, jobject javaObject) {

		int pixels = 1920 * 1080;
		jintArray pixelValues = env->NewIntArray(pixels);
		jint *narr = env->GetIntArrayElements(pixelValues, NULL);

        //Rainbows!
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
		}
		
		env->ReleaseIntArrayElements(pixelValues, narr, NULL);
		return pixelValues;
}