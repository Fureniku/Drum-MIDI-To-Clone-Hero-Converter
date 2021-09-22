package com.fureniku.miditochdrums.panels;

import com.fureniku.miditochdrums.ConverterScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelButtons extends PanelUI {

    public JButton clear = new JButton("Clear");
    public JButton convert = new JButton("Convert!");
    public JButton copy = new JButton("Copy");

    GridBagLayout layout = new GridBagLayout();

    public PanelButtons(ConverterScreen parent) {
        parentConstraints.gridx = 0;
        parentConstraints.gridy = 3;
        parentConstraints.gridwidth = 2;
        parentConstraints.fill = GridBagConstraints.BOTH;

        convert.setEnabled(false);

        convert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.convert();

            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.clear();
            }
        });

        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(parent.getOutput());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });

        /*this.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.insets = new Insets(10,10,10,5);
        c.anchor = GridBagConstraints.CENTER;
        this.add(clear, c);

        c.gridx = 1;
        c.insets = new Insets(10,5,10,5);
        c.anchor = GridBagConstraints.CENTER;
        this.add(convert, c);

        c.gridx = 2;
        c.insets = new Insets(10,5,10,10);
        c.anchor = GridBagConstraints.CENTER;
        this.add(copy, c);*/

        this.add(clear);
        this.add(convert);
        this.add(copy);
    }
}
