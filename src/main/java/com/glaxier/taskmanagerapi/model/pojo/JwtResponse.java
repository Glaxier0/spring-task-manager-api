package com.glaxier.taskmanagerapi.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class JwtResponse {
    private String id;
    private String username;
    private String email;
    private Integer age;
    private String token;
}
