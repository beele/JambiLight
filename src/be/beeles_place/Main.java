package be.beeles_place;

import be.beeles_place.controllers.ColorController;
import be.beeles_place.model.SettingsModel;
import be.beeles_place.modes.AbstractColorMode;
import be.beeles_place.modes.impl.AmbilightMode;
import be.beeles_place.view.ScreenGridView;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        SettingsModel settings = new SettingsModel();
        settings.setEnhanceColor(false);
        settings.setHorizontalRegions(128);
        settings.setVerticalRegions(80);
        settings.setIgnoreCenterRegions(false);
        settings.setPixelIteratorStepSize(1);
        settings.setRegionMargin(2);

        final ScreenGridView frame = new ScreenGridView(settings);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Show ui.
                frame.setSize(new Dimension(800,600));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

        ColorController c = new ColorController();
        AbstractColorMode mode = new AmbilightMode(settings, frame);
        c.setColorMode(mode);
    }
}
