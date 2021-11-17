package com.fureniku.miditochdrums.panels;

import com.fureniku.miditochdrums.ConverterScreen;

import javax.swing.*;
import java.awt.*;

public class PanelOptions extends PanelUI {

    JTextField startPos = new JTextField("768");
    JTextField kickTime = new JTextField("96");
    JTextField channelId = new JTextField("10");
    JTextField midiTicks = new JTextField("1920");
    JTextField chTicks = new JTextField("768");
    JCheckBox fullFile = new JCheckBox("Full File", false);
    JCheckBox auto2xKick = new JCheckBox("Auto 2x Kick",true);
    JCheckBox autoToms = new JCheckBox("Auto Tom Optimisation",true);
    JLabel startPosLabel = new JLabel("Drum Start Time:");
    JLabel kickTimeLabel = new JLabel("Max Kick Time:");
    JLabel channelIdLabel = new JLabel("Drum MIDI Channel ID (Default is 10):");
    JLabel ticksLabel = new JLabel("< MIDI ticks to Clone Hero Ticks >");

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
        c.weightx = 0.7;
        c.insets = new Insets(5,10,10,5);
        c.anchor = GridBagConstraints.CENTER;
        this.add(autoToms, c);

        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.5;
        c.insets = new Insets(5,5,5,10);
        c.anchor = GridBagConstraints.CENTER;
        this.add(channelIdLabel, c);

        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 0.5;
        c.insets = new Insets(5,5,5,10);
        c.anchor = GridBagConstraints.CENTER;
        this.add(channelId, c);

        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0.25;
        c.insets = new Insets(5,10,10,5);
        c.anchor = GridBagConstraints.CENTER;
        this.add(midiTicks, c);

        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 0.5;
        c.insets = new Insets(5,5,10,5);
        c.anchor = GridBagConstraints.CENTER;
        this.add(ticksLabel, c);

        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 0.25;
        c.insets = new Insets(5,5,10,10);
        c.anchor = GridBagConstraints.CENTER;
        this.add(chTicks, c);
    }

    public boolean shouldCreateFullFile() {
        System.out.println("Checking for full file status: " + fullFile.isSelected());
        return fullFile.isSelected();
    }

    public boolean shouldAuto2xKick() {
        return auto2xKick.isSelected();
    }

    public boolean shouldAutoToms() { return autoToms.isSelected(); }

    public int getKickTime() {
        return Integer.valueOf(kickTime.getText());
    }

    public int getStartTime() {
        return Integer.valueOf(startPos.getText());
    }

    public int[] getMidiChannels() {
        String[] str = channelId.getText().replaceAll(" ", "").split(",");
        int[] out = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            out[i] = Integer.valueOf(str[i]);
        }

        return out;
    }

    public boolean isValidChannel(int channel) {
        int[] channels = getMidiChannels();
        for (int i = 0; i < channels.length; i++) {
            if (channels[i]-1 == channel) {
                return true;
            }
        }
        return false;
    }

    public double getMIDIRatio() {
        return Double.valueOf(midiTicks.getText()) / Double.valueOf(chTicks.getText());
    }
}
