package com.aktionen.agrar.users.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private int id;
    private String username;
    private String email;
    private String password;
    private boolean loggedIn;

}