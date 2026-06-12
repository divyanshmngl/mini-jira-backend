package com.minijira.customException;

import com.minijira.enums.MessageEnum;
import lombok.Getter;


@Getter
public class TaskException extends RuntimeException {

    private final MessageEnum messageEnum;

    public TaskException(MessageEnum messageEnum) {
        super(messageEnum.getMessage());
        this.messageEnum = messageEnum;
    }
}
