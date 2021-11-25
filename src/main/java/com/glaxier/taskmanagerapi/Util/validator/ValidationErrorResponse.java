package com.glaxier.taskmanagerapi.Util.validator;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private List<Violation> violations = new ArrayList<>();

    @Override
    public String toString() {
        return "ValidationErrorResponse{" +
                "=" + violations +
                '}';
    }
}
