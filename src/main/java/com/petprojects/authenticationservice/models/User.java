package com.petprojects.authenticationservice.models;

public class User {
    private Long user_id;
    private String user_login;
    private String user_password;
    private String user_email;
    private String code_confirmation;

    public String getUser_email() {
        return user_email;
    }

    public String getCode_confirmation() {
        return code_confirmation;
    }

    public void setCode_confirmation(String code_confirmation) {
        this.code_confirmation = code_confirmation;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public User() {
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}
