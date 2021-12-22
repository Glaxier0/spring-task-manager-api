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

    public JwtResponse(String id, String username, String email, Integer age) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
    }
}
