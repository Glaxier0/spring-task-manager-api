package com.glaxier.taskmanagerapi.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class JwtResponse {
    private String id;
    private String username;
    private String email;
    private Integer age;
    private String token;
}
