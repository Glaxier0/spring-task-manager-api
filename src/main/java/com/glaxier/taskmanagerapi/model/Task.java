package com.glaxier.taskmanagerapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document
@Getter
@Setter
@ToString
public class Task {

    @Id
    private String id;

    @NotBlank
    private String description;
    private Boolean completed = false;
    private String userId;
}
