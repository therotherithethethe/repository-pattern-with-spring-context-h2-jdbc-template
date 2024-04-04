package com.arakviel.persistence.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements GenericEntity {

    private UUID id;
    private String login;
    private String password;
    private int age;
}
