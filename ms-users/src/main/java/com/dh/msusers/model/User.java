package com.dh.msusers.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private String id;
    private String username;
    private String email;
    private String firstName;

    private List<Bill> bills;

    public User(String id, String username, String email, String firstName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;

    }
}
