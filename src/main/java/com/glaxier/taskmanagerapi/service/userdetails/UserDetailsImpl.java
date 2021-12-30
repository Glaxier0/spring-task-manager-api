package com.glaxier.taskmanagerapi.service.userdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.glaxier.taskmanagerapi.model.Users;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private String id;

    private String username;
    private String email;

    @JsonIgnore
    private String password;
    private Integer age;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(Users user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getAge(),
                new ArrayList<>());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        UserDetailsImpl user = (UserDetailsImpl) object;
        return Objects.equals(id, user.id);
    }
}
