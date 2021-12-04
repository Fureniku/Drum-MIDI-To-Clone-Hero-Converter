package com.fureniku.miditochdrums;

public class MIDIToCHDrums {

    public static final String version = "1.2.1";

    public static void main(String[] args) {
        System.out.println("Starting program");

        ConverterScreen frame = new ConverterScreen();
    }

    public static void log(long tick, String msg, String... args) {
        System.out.printf("[" + tick + "] " + msg + "\n", args);
    }
}
