package com.arakviel.persistance.entity;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private UUID id;
    private String login;
    private String password;
    private int age;
}
