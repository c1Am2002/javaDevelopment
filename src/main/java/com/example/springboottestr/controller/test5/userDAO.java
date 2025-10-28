package com.example.springboottestr.controller.test5;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "test")
public class userDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public userDAO(String name) {
        this.name = name;
    }

    public userDAO() {

    }
}
