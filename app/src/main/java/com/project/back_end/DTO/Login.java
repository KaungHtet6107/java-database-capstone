package com.project.back_end.DTO;

public class Login {

    // Email address used for login
    private String email;

    // Password used for authentication
    private String password;

    // Default constructor
    public Login() {
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

}