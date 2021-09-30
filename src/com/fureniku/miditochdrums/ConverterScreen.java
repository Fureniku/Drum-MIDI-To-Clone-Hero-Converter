package com.fureniku.miditochdrums;

import com.fureniku.miditochdrums.panels.*;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ConverterScreen extends JFrame {

    private File midiFile = null;

    long firstTick = 0;

    PanelFile panelFile = new PanelFile(this);
    PanelOptions panelOptions = new PanelOptions(this);
    PanelNotes panelNotes = new PanelNotes(this);
    PanelOutput panelOutput = new PanelOutput(this);
    PanelButtons panelButtons = new PanelButtons(this);

    public ConverterScreen() {
        this.setPreferredSize(new Dimension(800, 600));
        this.setLayout(new GridBagLayout());
        this.setTitle("Drum MIDI to Clone Hero Chart Converter (v" + MIDIToCHDrums.version + ")");

        this.getContentPane().add(panelFile, panelFile.getConstraints());
        this.getContentPane().add(panelOptions, panelOptions.getConstraints());
        this.getContentPane().add(panelNotes, panelNotes.getConstraints());
        this.getContentPane().add(panelOutput, panelOutput.getConstraints());
        this.getContentPane().add(panelButtons, panelButtons.getConstraints());

        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
    }

    public void setMidiFile(File midiFile) {
        this.midiFile = midiFile;
        panelButtons.convert.setEnabled(true);
        panelButtons.generateFile.setEnabled(true);
    }

    public void clear() {
        panelOutput.clearList();
        panelOutput.refreshText(panelOptions);
        firstTick = -1;
    }

    public String getOutput() {
        return panelOutput.getOutput().getText();
    }

    public void convert() {
        clear();
        try {
            Sequence seq = MidiSystem.getSequence(midiFile);

            System.out.println("Loaded MIDI file, which has " + seq.getTracks().length + " tracks.");
            System.out.println("Converting with a ratio of " + panelOptions.getMIDIRatio());

            for (int i = 0; i < seq.getTracks().length; i++) {
                Track track = seq.getTracks()[i];
                int[] channelActivity = new int[16];
                System.out.println("Starting track " + i + " which has " + track.size() + " messages.");

                long lastKick = 0;
                boolean lastWasDouble = true; //Default to true so first kick will always be normal

                for (int j = 0; j < track.size(); j++) {

                    MidiEvent event = track.get(j);
                    MidiMessage msg = event.getMessage();

                    if (msg instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) msg;
                        channelActivity[sm.getChannel()]++;
                        if (panelOptions.isValidChannel(sm.getChannel())) {
                            if (sm.getCommand() == ShortMessage.NOTE_ON) {
                                long tick = Math.round(event.getTick() / panelOptions.getMIDIRatio());
                                if (firstTick <= 0) {
                                    firstTick = tick;
                                }
                                int key = sm.getData1();
                                if (panelNotes.isKick(key)) {
                                    if (panelOptions.shouldAuto2xKick()) {
                                        if (!lastWasDouble && lastKick + panelOptions.getKickTime() > tick) {
                                            lastWasDouble = true;
                                            printNote(tick, 32, false);
                                        } else {
                                            lastWasDouble = false;
                                            printNote(tick, 0, false);
                                        }
                                        lastKick = tick;
                                    } else {
                                        printNote(tick, 0, false);
                                    }
                                } else if (panelNotes.isRed(key)) { //snare
                                    printNote(tick, 1, false);
                                } else if (panelNotes.isYellowCymbal(key)) { //hi hat open/closed / splash
                                    printNote(tick, 2, true);
                                } else if (panelNotes.isGreenCymbal(key)) { //crash
                                    printNote(tick, 4, true);
                                } else if (panelNotes.isBlueCymbal(key)) { //ride
                                    printNote(tick, 3, true);
                                } else if (panelNotes.isGreen(key)) { //floor tom
                                    printNote(tick, 4, false);
                                } else if (panelNotes.isBlue(key)) { //mid tom
                                    printNote(tick, 3, false);
                                } else if (panelNotes.isYellow(key)) { //high tom
                                    printNote(tick, 2, false);
                                } else if (!panelNotes.isIgnored(key)) {
                                    panelOutput.addError("Unknown drum ID " + key + " at position " + tick + "\n");
                                }
                            }
                        }
                    }
                }
                if (panelOutput.isListEmpty()) {
                    panelOutput.addLog("\nNo notes found on specified channel. Channel activity on track " + i + " as follows:\n");
                    for (int k = 0; k < channelActivity.length; k++) {
                        panelOutput.addLog("Channel " + (k+1) + ": " + channelActivity[k] + " Notes");
                    }
                }
            }
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        panelOutput.refreshText(panelOptions);
    }

    public void printNote(long tick, int id, boolean cymbal) {
        long tickFinal = tick - firstTick + panelOptions.getStartTime();
        panelOutput.addToList("  " + tickFinal + " = N " + id + " 0");
        if (cymbal) {
            panelOutput.addToList("  " + tickFinal + " = N " + (id + 64) + " 0");
        }
    }
}
