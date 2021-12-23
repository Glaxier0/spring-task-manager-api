package com.glaxier.taskmanagerapi.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProfileResponse {
    private String id;
    private String username;
    private String email;
    private Integer age;
}
