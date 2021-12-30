package com.glaxier.taskmanagerapi.model.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class UpdateTask {

    @Null
    private String id;
    @Size(min = 1, message = "Description length must be minimum 1.")
    private String description;
    private Boolean completed;

    @Null
    private String userId;
}
