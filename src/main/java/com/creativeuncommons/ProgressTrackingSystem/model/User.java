package com.creativeuncommons.ProgressTrackingSystem.model;

import com.creativeuncommons.ProgressTrackingSystem.query.USER_SCHEMA_DATA;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private UUID id;

    private String userName;

    private String password;

    private String salt;

    private String email;
    private List<Role> roles;

    public User(UUID id, String userName, String password, String salt, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.salt = salt;
        this.email = email;
        roles = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setRoles(List<Role> roles){
        this.roles = roles;
    }
    public List<Role> getRoles(){
        return roles;
    }


    public static User buildFromMap(Map<String, Object> userRow) {
        User user = new User((UUID) userRow.get(USER_SCHEMA_DATA.ID_COLUMN.toString()),
                (String) userRow.get(USER_SCHEMA_DATA.USER_NAME_COLUMN.toString()),
                (String) userRow.get(USER_SCHEMA_DATA.USER_PASSWORD_COLUMN.toString()),
                (String) userRow.get(USER_SCHEMA_DATA.SALT_COLUMN.toString()),
                (String) userRow.get(USER_SCHEMA_DATA.USER_EMAIL_COLUMN.toString()));
        return user;
    }
}
