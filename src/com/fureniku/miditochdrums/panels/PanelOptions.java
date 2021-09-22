package com.fureniku.miditochdrums.panels;

import com.fureniku.miditochdrums.ConverterScreen;

import javax.swing.*;
import java.awt.*;

public class PanelOptions extends PanelUI {

    JTextField startPos = new JTextField("768");
    JTextField kickTime = new JTextField("96");
    JTextField channelId = new JTextField("10");
    JCheckBox fullFile = new JCheckBox("Full File", true);
    JCheckBox auto2xKick = new JCheckBox("Auto 2x Kick",true);
    JLabel startPosLabel = new JLabel("Drum Start Time:");
    JLabel kickTimeLabel = new JLabel("Max Kick Time:");
    JLabel channelIdLabel = new JLabel("Drum MIDI Channel ID (Default is 10):");

    GridBagLayout layout = new GridBagLayout();

    public PanelOptions(ConverterScreen parent) {
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        parentConstraints.insets = new Insets(5,5,5,5);
        parentConstraints.gridx = 0;
        parentConstraints.gridy = 1;
        parentConstraints.fill = GridBagConstraints.BOTH;

        this.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10,10,5,5);
        c.anchor = GridBagConstraints.CENTER;
        this.add(fullFile, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.2;
        c.insets = new Insets(10,5,5,5);
        c.anchor = GridBagConstraints.CENTER;
        this.add(startPosLabel, c);

        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(10,5,5,10);
        c.anchor = GridBagConstraints.CENTER;
        this.add(startPos, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.7;
        c.insets = new Insets(5,10,10,5);
        c.anchor = GridBagConstraints.CENTER;
        this.add(auto2xKick, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.2;
        c.insets = new Insets(5,5,5,5);
        c.anchor = GridBagConstraints.CENTER;
        this.add(kickTimeLabel, c);

        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0.7;
        c.insets = new Insets(5,5,5,10);
        c.anchor = GridBagConstraints.CENTER;
        this.add(kickTime, c);

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.5;
        c.insets = new Insets(5,5,10,10);
        c.anchor = GridBagConstraints.CENTER;
        this.add(channelIdLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.5;
        c.insets = new Insets(5,10,10,5);
        c.anchor = GridBagConstraints.CENTER;
        this.add(channelId, c);
    }

    public boolean shouldCreateFullFile() {
        System.out.println("Checking for full file status: " + fullFile.isSelected());
        return fullFile.isSelected();
    }

    public boolean shouldAuto2xKick() {
        return auto2xKick.isSelected();
    }

    public int getKickTime() {
        return Integer.valueOf(kickTime.getText());
    }

    public int getStartTime() {
        return Integer.valueOf(startPos.getText());
    }

    public int getMidiChannel() {
        return Integer.valueOf(channelId.getText());
    }
}
