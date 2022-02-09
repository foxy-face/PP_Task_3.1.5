package org.example;

import org.example.configuration.MyConfig;
import org.example.model.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main( String[] args ) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);
        Communication communication = context.getBean("communication", Communication.class);

        communication.getAllUsers();

        communication.saveUser(new User(3L, "James", "Brown", (byte) 24));

        communication.updateUser(new User(3L, "Thomas", "Shelby", (byte) 24));

        communication.deleteUser(3L);

    }
}
