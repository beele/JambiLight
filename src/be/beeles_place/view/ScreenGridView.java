package be.beeles_place.view;

import be.beeles_place.model.SettingsModel;

import javax.swing.*;
import java.awt.*;

public class ScreenGridView extends JFrame {

    private GridLayout gLayout;
    private final Container container;
    private SettingsModel settings;

    public ScreenGridView(SettingsModel settings) {
        super("PixelTest");
        setResizable(true);

        this.settings = settings;
        gLayout = new GridLayout(settings.getVerticalRegions(),settings.getHorizontalRegions());
        container = super.getContentPane();
        makeUI();
    }

    public void makeUI() {
        container.setSize(new Dimension(800,600));
        container.setLayout(gLayout);

        int regions = settings.getHorizontalRegions() * settings.getVerticalRegions();
        for(int i = 0; i < regions ; i++) {
            JPanel pnl = new JPanel();
            pnl.setBackground(Color.BLACK);
            container.add(pnl);
        }
    }

    public void updateView(String title, int[][][]regions) {
        super.setTitle(title);
        final Container cont = super.getContentPane();

        int compCount = 0;
        for(int i = 0 ; i < settings.getVerticalRegions() ; i++) {
            for(int j = 0 ; j < settings.getHorizontalRegions() ; j++) {
                JPanel pnl = (JPanel)container.getComponent(compCount++);
                Color c = new Color(    regions[j][i][0],
                                        regions[j][i][1],
                                        regions[j][i][2]);
                if(pnl.getBackground().equals(c) == false) {
                    pnl.setBackground(c);
                }
            }
        }

        /*JPanel pnl = (JPanel)container.getComponent(0);
        pnl.setBackground(Color.green);
        JPanel pnl2 = (JPanel)container.getComponent(5);
        pnl2.setBackground(Color.green);
        JPanel pnl3 = (JPanel)container.getComponent(10);
        pnl3.setBackground(Color.green);
        JPanel pnl4 = (JPanel)container.getComponent(15);
        pnl4.setBackground(Color.green);

        JPanel pnl5 = (JPanel)container.getComponent(32);
        pnl5.setBackground(Color.green);
        JPanel pnl6 = (JPanel)container.getComponent(37);
        pnl6.setBackground(Color.green);
        JPanel pnl7 = (JPanel)container.getComponent(42);
        pnl7.setBackground(Color.green);
        JPanel pnl8 = (JPanel)container.getComponent(47);
        pnl8.setBackground(Color.green);*/
    }
}