package com.glaxier.taskmanagerapi.model.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class UpdateUser {

    @Null(message = "Id update is forbidden.")
    private String id;
    @Size(min = 1, message = "Name length must be minimum 1.")
    private String name;
    @Null(message = "Email update is forbidden.")
    private String email;
    @Size(min = 8, message = "Password length must be minimum 8.")
    private String password;
    @PositiveOrZero
    private Integer age;
}
