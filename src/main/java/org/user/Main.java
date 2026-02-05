package org.user;

import org.user.exception.UserNotFoundException;
import org.user.model.User;
import org.user.service.InMemoryUserService;
import org.user.service.UserService;


public class Main {
    //public static void main(String[] args) {
//        System.out.printf("Hello and welcome!");
//

        public static void main(String[] args) {
            UserService service = new InMemoryUserService();

            //service.createUser(new User(1L, "Alice", "alice@test.com"));
            try {
                service.createUser(new User(1L, "Alice", "alice@test.com"));
                service.createUser(new User(2L, "Bob", "bob@test.com"));
                System.out.println(service.getUserById(1L).getName());
                System.out.println(service.getUserById(2L).getName());

                service.updateUser(1L, new User(999L, "Alice Updated", "alice.updated@test.com"));
                System.out.println(service.getUserById(1L).getEmail());

                service.deleteUser(1L);

                service.getUserById(1L);
            } catch (UserNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }