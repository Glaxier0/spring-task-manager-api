package com.glaxier.taskmanagerapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Document
@ToString
@Getter
@Setter
public class Users {

    @Id
    private String id;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password length must be minimum 8.")
    private String password;

    @PositiveOrZero(message = "Age must be greater than or equal to 0.")
    private Integer age;
    private List<String> tokens = new ArrayList<>();
    private String avatar;

    public Users(String name, String email, String password, Integer age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public void updateUser(UpdateUser updateUser) {
        this.name = updateUser.getName() == null ? this.name : updateUser.getName();
        this.password = updateUser.getPassword() == null ? this.password : updateUser.getPassword();
        this.age = updateUser.getAge() == null ? this.age : updateUser.getAge();
    }
}
