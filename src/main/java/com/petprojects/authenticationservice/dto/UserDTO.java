package com.petprojects.authenticationservice.dto;

public class UserDTO {

    private String user_login;
    private String user_password;
    private String user_email;
    private String code_confirmation;

    public UserDTO() {
    }

    public String getCode_confirmation() {
        return code_confirmation;
    }

    public void setCode_confirmation(String code_confirmation) {
        this.code_confirmation = code_confirmation;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
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
