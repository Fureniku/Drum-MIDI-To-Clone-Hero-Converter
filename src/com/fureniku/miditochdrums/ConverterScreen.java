package com.fureniku.miditochdrums;

import com.fureniku.miditochdrums.panels.*;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ConverterScreen extends JFrame {

    private File midiFile = null;

    long firstTick = 0;

    PanelFile panelFile = new PanelFile(this);
    PanelOptions panelOptions = new PanelOptions(this);
    PanelNotes panelNotes = new PanelNotes(this);
    PanelOutput panelOutput = new PanelOutput(this);
    PanelButtons panelButtons = new PanelButtons(this);

    //Used for ensuring drum sanity
    long prevTick = 0;

    DrumObject drum1 = null;
    DrumObject drum2 = null;
    DrumObject drumKick = null;

    ArrayList<Integer> keys = new ArrayList<Integer>();

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
        keys.clear();
        drum1 = null;
        drum2 = null;
        drumKick = null;
        firstTick = -1;
        prevTick = 0;
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

                if (track.size() < 5) {
                    continue;
                }

                long lastKick = 0;
                boolean lastWasDouble = true; //Default to true so first kick will always be normal

                if (panelOptions.shouldAutoToms()) {
                    generateKeyList(track, channelActivity);
                    autoAssignToms();
                }

                for (int j = 0; j < track.size(); j++) {

                    MidiEvent event = track.get(j);
                    MidiMessage msg = event.getMessage();

                    if (msg instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) msg;
                        channelActivity[sm.getChannel()]++;
                        if (panelOptions.isValidChannel(sm.getChannel())) {
                            if (sm.getCommand() == ShortMessage.NOTE_ON) {
                                long tick = Math.round(event.getTick() / panelOptions.getMIDIRatio());
                                if (firstTick < 0) {
                                    firstTick = tick;
                                }
                                int key = sm.getData1();

                                if (panelNotes.isKick(key)) {
                                    if (panelOptions.shouldAuto2xKick()) {
                                        if (!lastWasDouble && lastKick + panelOptions.getKickTime() >= tick) {
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
                    panelOutput.addLog("\nSet channel ID to -1 to use all available channels.");
                }
            }

            writeNoteToFile(prevTick);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        panelOutput.refreshText(panelOptions);
    }

    private void generateKeyList(Track track, int[] channelActivity) {
        for (int j = 0; j < track.size(); j++) {

            MidiEvent event = track.get(j);
            MidiMessage msg = event.getMessage();

            if (msg instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) msg;
                channelActivity[sm.getChannel()]++;
                if (panelOptions.isValidChannel(sm.getChannel())) {
                    if (sm.getCommand() == ShortMessage.NOTE_ON) {
                        int key = sm.getData1();

                        if (!keys.contains(key)) {
                            keys.add(key);
                        }
                    }
                }
            }
        }
    }

    private void autoAssignToms() {
        ArrayList<Integer> programToms = new ArrayList<Integer>(); //All the toms registered in the program
        ArrayList<Integer> finalUseToms = new ArrayList<Integer>(); //The intersect of registered toms that are actually being used in this song

        for (int i = 35; i < 81; i++) { //Drum MIDI standard has notes between 35 and 81
            if (panelNotes.isYellow(i) || panelNotes.isBlue(i) || panelNotes.isGreen(i)) {
                programToms.add(i);
            }
        }

        for (int i = 0; i < programToms.size(); i++) {
            if (keys.contains(programToms.get(i))) {
                finalUseToms.add(programToms.get(i));
            }
        }

        finalUseToms.sort(Comparator.naturalOrder());
        //This isn't nice, but it's 1:30am, and I'm tired, and it *works* so I dont care. maybe I'll improve later.
        if (finalUseToms.size() == 1) {
            panelNotes.setBlue(finalUseToms.get(0) + "");
        }

        if (finalUseToms.size() == 2) {
            panelNotes.setBlue(finalUseToms.get(0) + "");
            panelNotes.setGreen(finalUseToms.get(1) + "");
        }

        if (finalUseToms.size() == 3) {
            System.out.println("Holy grail detected!");
            panelNotes.setYellow(finalUseToms.get(2) + "");
            panelNotes.setBlue(finalUseToms.get(1) + "");
            panelNotes.setGreen(finalUseToms.get(0) + "");
        }

        if (finalUseToms.size() == 4) {
            panelNotes.setYellow(finalUseToms.get(3) + "");
            panelNotes.setBlue(finalUseToms.get(2) + "");
            panelNotes.setGreen(finalUseToms.get(1) + "," + finalUseToms.get(0));
        }

        if (finalUseToms.size() == 5) {
            panelNotes.setYellow(finalUseToms.get(4) + "");
            panelNotes.setBlue(finalUseToms.get(3) + "," + finalUseToms.get(2));
            panelNotes.setGreen(finalUseToms.get(1) + "," + finalUseToms.get(0));
        }

        if (finalUseToms.size() == 6) {
            panelNotes.setYellow(finalUseToms.get(5) + ","+ finalUseToms.get(4));
            panelNotes.setBlue(finalUseToms.get(3) + "," + finalUseToms.get(2));
            panelNotes.setGreen(finalUseToms.get(1) + "," + finalUseToms.get(0));
        }
    }

    public void printNote(long tick, int id, boolean cymbal) {
        long tickFinal = tick - firstTick + panelOptions.getStartTime();

        if (prevTick != tickFinal) {
            writeNoteToFile(tickFinal);
        }

        DrumObject drum = new DrumObject(id, tickFinal, cymbal);

        if (drum.isKick()) { //If it's a kick, set the kick
            drumKick = drum;
        } else {
            if (drum1 == null) { //If drum 1 is empty, use it
                drum1 = drum;
            } else if (drum2 == null) { //If drum 2 is empty, use it
                drum2 = drum;
            } else { //Both hands are full, add no more drums.
                return;
            }
        }

        prevTick = tickFinal;
    }

    private void writeNoteToFile(long tick) {
        //Write drums to file, then clear list
        if (drumKick != null) {
            drumKick.addToList(panelOutput);
        }

        //If both hands are being used, run extra checks
        if (drum1 != null && drum2 != null) {
            System.out.println("Starting 2-hand drum processing at " + tick);
            //If both are cymbals, force them to yellow/green for a double crash
            if (drum1.isCymbal() && drum2.isCymbal() && (drum1.getId() == drum2.getId())) {
                MIDIToCHDrums.log(tick, "Two cymbal hits detected; ensuring separation");
                if (drum1.getId() == Drums.GREEN.getId()) {
                    drum1.setId(Drums.YELLOW.getId());
                } else {
                    drum1.setId(Drums.GREEN.getId());
                }
            }
            //If both drum hits are the same, move one as appropriate
            if (drum1.getId() == drum2.getId() && drum1.isCymbal() == drum2.isCymbal()) {
                System.out.println("Double drum hit on " + drum1.getId());
                if (drum1.getId() == Drums.RED.getId()) {
                    drum2.setId(Drums.YELLOW.getId());
                }
                if (drum1.getId() == Drums.YELLOW.getId()) {
                    drum2.setId(Drums.BLUE.getId());
                }
                if (drum1.getId() == Drums.BLUE.getId()) {
                    drum2.setId(Drums.GREEN.getId());
                }
                if (drum1.getId() == Drums.GREEN.getId()) {
                    drum2.setId(Drums.YELLOW.getId());
                }
            }
            drum1.addToList(panelOutput);
            drum2.addToList(panelOutput);

        } else { //Else, just place the drum we have.
            if (drum1 != null) drum1.addToList(panelOutput);
            if (drum2 != null) drum2.addToList(panelOutput);
        }

        //Reset drums to null ready for next tick
        drum1 = null;
        drum2 = null;
        drumKick = null;
    }
}
