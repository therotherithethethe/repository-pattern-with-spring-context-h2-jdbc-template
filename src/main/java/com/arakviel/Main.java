package com.arakviel;

import com.arakviel.config.ApplicationConfig;
import com.arakviel.persistance.entity.Parrot;
import com.arakviel.persistance.entity.User;
import com.arakviel.persistance.repository.contracts.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        Parrot boklah = context.getBean(Parrot.class);
        System.out.println(boklah.getName());

        pseudoTests(context);
    }

    private static void pseudoTests(AnnotationConfigApplicationContext context) {
        UserRepository userRepository = context.getBean(UserRepository.class);
        List<User> users = userRepository.findAll();
        User defaultUser = User.builder()
            .id(UUID.randomUUID())
            .login("user")
            .password("password")
            .age(1)
            .build();
        User user = userRepository.findById(users.get(1).getId()).orElse(defaultUser);

        User addedUser = userRepository.save(User.builder()
            .login("gludzik")
            .password("password")
            .age(17)
            .build());

        User userGludzik = userRepository.findById(addedUser.getId()).orElse(defaultUser);
        userGludzik.setAge(50);

        userRepository.save(userGludzik);
        userRepository.delete(userGludzik.getId());

        System.out.println(users);
        System.out.println(user);
        System.out.println(userGludzik);
        System.out.println(userRepository.findByLogin("geletiy").orElse(defaultUser));
    }
}