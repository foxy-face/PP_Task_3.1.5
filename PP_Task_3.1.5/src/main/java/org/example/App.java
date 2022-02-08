package org.example;

import org.example.configuration.MyConfig;
import org.example.model.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Objects;

public class App {
    public static void main( String[] args ) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);
        Communication communication = context.getBean("communication", Communication.class);

        System.out.println(communication.getAllUsers());

        communication.saveUser(new User(3L, "James", "Brown", (byte) 24));

    }
}
