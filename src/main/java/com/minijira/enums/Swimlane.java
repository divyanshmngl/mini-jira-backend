package com.minijira.enums;

public enum Swimlane {
    TODO,
    IN_PROGRESS,
    DONE;

    public boolean isTerminal() {
        return this == DONE;
    }
}
