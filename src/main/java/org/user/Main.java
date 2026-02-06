package org.user;

import io.vertx.core.Vertx;
import org.user.exception.UserNotFoundException;
import org.user.model.User;
import org.user.service.InMemoryUserService;
import org.user.service.UserService;
import org.user.web.UserVerticle;


public class Main {
    //public static void main(String[] args) {
//        System.out.printf("Hello and welcome!");
//

        public static void main(String[] args) {
            UserService service = new InMemoryUserService();

            //service.createUser(new User(1L, "Alex", "alex@test.com"));
//            try {
//                service.createUser(new User(1L, "Alex", "alex@test.com"));
//                service.createUser(new User(2L, "Bob", "bob@test.com"));
//                System.out.println(service.getUserById(1L).getName());
//                System.out.println(service.getUserById(2L).getName());
//
//                service.updateUser(1L, new User(999L, "Alex Updated", "alex.updated@test.com"));
//                System.out.println(service.getUserById(1L).getEmail());
//
//                service.deleteUser(1L);
//
//                service.getUserById(1L);
//            } catch (UserNotFoundException e) {
//                throw new RuntimeException(e);
//            }
            Vertx vertx = Vertx.vertx();

            UserService userService = new InMemoryUserService();
            vertx.deployVerticle(new UserVerticle(userService));

        }
    }