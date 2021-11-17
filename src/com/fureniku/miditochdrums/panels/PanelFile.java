package com.fureniku.miditochdrums.panels;

import com.fureniku.miditochdrums.ConverterScreen;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PanelFile extends PanelUI {

    JButton openFile = new JButton("Open File");
    JTextArea fileDir = new JTextArea(1, 10);

    GridBagLayout layout = new GridBagLayout();

    public PanelFile(ConverterScreen parent) {
        parentConstraints.gridx = 0;
        parentConstraints.gridy = 0;
        parentConstraints.fill = GridBagConstraints.BOTH;
        parentConstraints.weightx = 0.85;

        fileDir.setText("Click Open File to get started.");

        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("."));
                FileFilter filter = new FileNameExtensionFilter("MIDI file", "mid", "midi");
                chooser.setFileFilter(filter);

                int choice = chooser.showOpenDialog(openFile.getParent());
                if (choice != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                File selected = chooser.getSelectedFile();

                if (getExtension(selected.getName()).equalsIgnoreCase("mid") || getExtension(selected.getName()).equalsIgnoreCase("midi")) {
                    parent.setMidiFile(chooser.getSelectedFile());
                    fileDir.setText(chooser.getSelectedFile().getAbsolutePath());
                } else {
                    System.out.println("Invalid file type " + getExtension(selected.getName()) + " selected, please try again.");
                }
            }
        });

        this.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.weightx = 0.8;
        c.insets = new Insets(0,10,0,5);
        c.anchor = GridBagConstraints.CENTER;
        JScrollPane jsp = new JScrollPane(fileDir);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(jsp, c);

        c.gridx = 1;
        c.weightx = 0.2;
        c.insets = new Insets(10,5,10,10);
        c.anchor = GridBagConstraints.LINE_END;
        this.add(openFile, c);
    }

    public String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
