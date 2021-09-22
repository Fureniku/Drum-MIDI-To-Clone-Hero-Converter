package com.fureniku.miditochdrums.panels;

import com.fureniku.miditochdrums.ConverterScreen;

import javax.swing.*;
import java.awt.*;

public class PanelNotes extends PanelUI {

    JLabel kickLabel = new JLabel("Kick");
    JLabel redLabel = new JLabel("Red Drum (Snare)");
    JLabel yellowLabel = new JLabel("Yellow Drum (High Tom)");
    JLabel blueLabel = new JLabel("Blue Drum (Mid Tom)");
    JLabel greenLabel = new JLabel("Green Drum (Floor Tom)");
    JLabel cymbalYellowLabel = new JLabel("Yellow Cymbal (Hi-Hat/Splash)");
    JLabel cymbalBlueLabel = new JLabel("Blue Cymbal (Ride/Crash 2)");
    JLabel cymbalGreenLabel = new JLabel("Green Cymbal (Crash)");
    JLabel ignoredLabel = new JLabel("Ignored Notes");

    JTextField kickText = new JTextField("36"); //0
    JTextField redText = new JTextField("38,40"); //1
    JTextField yellowText = new JTextField("48,50"); //2
    JTextField blueText = new JTextField("47"); //3
    JTextField greenText = new JTextField("41,43,45"); //4
    JTextField cymbalYellowText = new JTextField("42,46,54,55,95"); //2
    JTextField cymbalBlueText = new JTextField("52,53,59"); //3
    JTextField cymbalGreenText = new JTextField("49,57"); //4
    JTextField ignoredText = new JTextField("0");

    GridBagLayout layout = new GridBagLayout();

    public PanelNotes(ConverterScreen parent) {
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        parentConstraints.insets = new Insets(5,5,5,5);
        parentConstraints.gridx = 0;
        parentConstraints.gridy = 2;
        parentConstraints.fill = GridBagConstraints.BOTH;
        parentConstraints.weightx = 0.85;
        parentConstraints.weighty = 0.8;

        this.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 0.1;

        //Labels
        c.insets = new Insets(5,10,5,5);

        c.gridx = 0;
        c.gridy = 0;
        this.add(kickLabel, c);

        c.insets = new Insets(0,10,5,5);

        c.gridx = 0;
        c.gridy = 1;
        this.add(redLabel, c);

        c.gridx = 0;
        c.gridy = 2;
        this.add(yellowLabel, c);

        c.gridx = 0;
        c.gridy = 3;
        this.add(blueLabel, c);

        c.gridx = 0;
        c.gridy = 4;
        this.add(greenLabel, c);

        c.gridx = 0;
        c.gridy = 5;
        this.add(cymbalYellowLabel, c);

        c.gridx = 0;
        c.gridy = 6;
        this.add(cymbalBlueLabel, c);

        c.gridx = 0;
        c.gridy = 7;
        this.add(cymbalGreenLabel, c);

        c.gridx = 0;
        c.gridy = 8;
        this.add(ignoredLabel, c);

        //Text Fields
        c.insets = new Insets(0,5,5,10);
        c.weightx = 0.6;

        c.gridx = 1;
        c.gridy = 0;
        this.add(kickText, c);

        c.gridx = 1;
        c.gridy = 1;
        this.add(redText, c);

        c.gridx = 1;
        c.gridy = 2;
        this.add(yellowText, c);

        c.gridx = 1;
        c.gridy = 3;
        this.add(blueText, c);

        c.gridx = 1;
        c.gridy = 4;
        this.add(greenText, c);

        c.gridx = 1;
        c.gridy = 5;
        this.add(cymbalYellowText, c);

        c.gridx = 1;
        c.gridy = 6;
        this.add(cymbalBlueText, c);

        c.gridx = 1;
        c.gridy = 7;
        this.add(cymbalGreenText, c);

        c.gridx = 1;
        c.gridy = 8;
        this.add(ignoredText, c);
    }

    public boolean isKick(int id) { return isCorrectDrum(kickText, id); }
    public boolean isRed(int id) { return isCorrectDrum(redText, id); }
    public boolean isYellow(int id) { return isCorrectDrum(yellowText, id); }
    public boolean isBlue(int id) { return isCorrectDrum(blueText, id); }
    public boolean isGreen(int id) { return isCorrectDrum(greenText, id); }
    public boolean isYellowCymbal(int id) { return isCorrectDrum(cymbalYellowText, id); }
    public boolean isBlueCymbal(int id) { return isCorrectDrum(cymbalBlueText, id); }
    public boolean isGreenCymbal(int id) { return isCorrectDrum(cymbalGreenText, id); }
    public boolean isIgnored(int id) { return isCorrectDrum(ignoredText, id); }

    public boolean isCorrectDrum(JTextField text, int id) {
        String[] str = text.getText().replaceAll(" ", "").split(",");
        int[] out = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            if (Integer.valueOf(str[i]) == id) {
                return true;
            }
        }

        return false;
    }
}
