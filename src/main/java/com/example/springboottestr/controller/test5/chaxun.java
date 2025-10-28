package com.example.springboottestr.controller.test5;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface chaxun extends JpaRepository<userDAO, Long> {
    List <userDAO> findByName(String name);
}
