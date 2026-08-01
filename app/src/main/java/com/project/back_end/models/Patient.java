package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 100)
    private String name;


    @NotNull(message = "Email cannot be null")
    @Email
    private String email;


    @NotNull(message = "Password cannot be null")
    @Size(min = 6)
    private String password;


    @NotNull(message = "Phone cannot be null")
    @Pattern(
            regexp = "\\d{3}-\\d{3}-\\d{4}",
            message = "Phone number must be in format XXX-XXX-XXXX"
    )
    private String phone;


    @NotNull(message = "Address cannot be null")
    @Size(max = 255)
    private String address;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}