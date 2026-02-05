package org.user.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(long id) {
        super("User not found for id: " + id);
    }
}
