package be.beeles_place.jambiLight.utils;

import java.util.Locale;

public class OperatingSystemDetector {

    public enum OSType {
        MacOS,
        Windows,
        Linux,
        Other
    }

    private static OSType detectedOS;

    public static OSType detectOperatingSystem() {
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((OS.contains("mac")) || (OS.contains("darwin"))) {
                detectedOS = OSType.MacOS;
            } else if (OS.contains("win")) {
                detectedOS = OSType.Windows;
            } else if (OS.contains("nux")) {
                detectedOS = OSType.Linux;
            } else {
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }
}
