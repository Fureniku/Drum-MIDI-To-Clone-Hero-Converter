package com.fureniku.miditochdrums.panels;

import com.fureniku.miditochdrums.ConverterScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelOutput extends PanelUI {

    private ArrayList<String> outputList = new ArrayList<>();
    private ArrayList<String> errors = new ArrayList<>();
    private ArrayList<String> logs = new ArrayList<>();
    private JTextArea text = new JTextArea(32, 18);

    public PanelOutput(ConverterScreen parent) {
        parentConstraints.gridx = 1;
        parentConstraints.gridy = 0;
        parentConstraints.gridheight = 3;
        parentConstraints.fill = GridBagConstraints.BOTH;
        parentConstraints.anchor = GridBagConstraints.LINE_END;

        text.setBackground(Color.WHITE);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        JScrollPane jsp = new JScrollPane(text);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(jsp, c);
    }

    public void addToList(String str) {
        outputList.add(str);
    }

    public boolean isListEmpty() {
        return outputList.isEmpty();
    }

    public void addError(String str) {
        if (!errors.contains(str)) {
            errors.add(str);
        }
    }

    public void addLog(String str) {
        logs.add(str);
    }

    public void clearList() {
        outputList.clear();
        errors.clear();
        logs.clear();
        text.setText("");
        System.out.println("Output cleared");
    }

    public void refreshText(PanelOptions panelOptions) {
        if (errors.isEmpty()) {
            text.setLineWrap(false);
            text.setWrapStyleWord(false);
            if (!outputList.isEmpty()) {
                if (panelOptions.shouldCreateFullFile()) {
                    text.setText("[Song]\n");
                    text.append("{\n");
                    text.append("  Name = \"Drum Export\"\n");
                    text.append("  Offset = 0\n");
                    text.append("  Resolution = 192\n");
                    text.append("  Player2 = bass\n");
                    text.append("  Difficulty = 0\n");
                    text.append("  PreviewStart = 0\n");
                    text.append("  PreviewEnd = 0\n");
                    text.append("  Genre = \"rock\"\n");
                    text.append("  MediaType = \"cd\"\n");
                    text.append("}\n");
                    text.append("[SyncTrack]\n");
                    text.append("{\n");
                    text.append("  0 = TS 4\n");
                    text.append("  0 = B 135000\n");
                    text.append("}\n");
                    text.append("[Events]\n");
                    text.append("{\n");
                    text.append("}\n");
                    text.append("[ExpertDrums]\n");
                } else {
                    text.setText("[ExpertDrums]\n");
                }
                text.append("{\n");
                for (int i = 0; i < outputList.size(); i++) {
                    text.append(outputList.get(i) + "\n");
                }
                text.append("}");
            } else {
                text.setLineWrap(true);
                text.setWrapStyleWord(true);
                text.setText("There was an error detecting notes.\n");
                for (int i = 0; i < logs.size(); i++) {
                    text.append(logs.get(i) + "\n");
                }
            }
        } else {
            text.setLineWrap(true);
            text.setWrapStyleWord(true);
            text.setText("Some errors were found. Please make sure all IDs are mapped to relevant notes or the ignore list, and you are on the right channel, then try again.\n\n" +
                    "You may need to add <35 and >81 to the ignored notes to limit to standard drum range.\n\n");
            for (int i = 0; i < errors.size(); i++) {
                text.append(errors.get(i) + "\n");
            }
            text.append("\nThere are errors. Please scroll to the top of the output for more information.");
        }
    }

    public JTextArea getOutput() {
        return text;
    }
}
