package com.minijira.util;

import com.minijira.enums.Swimlane;

public class SwimlaneTransitionValidator {

    public static boolean isAllowed(Swimlane from, Swimlane to) {

        // same state allowed
        if (from == to) {
            return true;
        }

        // if already DONE, no transition allowed
        if (from == Swimlane.DONE) {
            return false;
        }

        if (from == Swimlane.TODO) {
            return to == Swimlane.IN_PROGRESS;
        }

        // IN-PROGRESS transitions
        if (from == Swimlane.IN_PROGRESS) {
            return (to == Swimlane.TODO || to == Swimlane.DONE);
        }

        return false;
    }
}