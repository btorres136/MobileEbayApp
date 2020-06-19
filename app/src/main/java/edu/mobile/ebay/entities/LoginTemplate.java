package edu.mobile.ebay.entities;

import java.io.Serializable;

public class LoginTemplate implements Serializable {
    private String username;
    private String password;

    public LoginTemplate(){}

    public LoginTemplate(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String Username) {
        this.username = Username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        this.password = Password;
    }
}
