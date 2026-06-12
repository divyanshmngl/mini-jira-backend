package com.minijira.enums;

import lombok.Getter;

@Getter
public enum ApiResponseEnum {

    SUCCESS("Success"),
    FAILURE("Failure"),
    SAVED("Record Saved Successfully"),
    UPDATED("Record Updated Successfully"),
    NO_DATA_AVAILABLE("No Data Available");

    private final String message;

    ApiResponseEnum(String message) {
        this.message = message;
    }
}
