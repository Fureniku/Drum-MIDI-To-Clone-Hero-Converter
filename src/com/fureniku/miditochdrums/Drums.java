package com.fureniku.miditochdrums;

public enum Drums {
    RED (1),
    YELLOW (2),
    BLUE (3),
    GREEN (4),
    KICK (0),
    KICK_DOUBLE(32);

    private final int drumId;

    private Drums(int id) {
        this.drumId = id;
    }

    public int getId() {
        return this.drumId;
    }
}