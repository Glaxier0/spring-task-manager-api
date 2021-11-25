package com.glaxier.taskmanagerapi.Util.validator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Violation {
    private String field;
    private String message;
}
