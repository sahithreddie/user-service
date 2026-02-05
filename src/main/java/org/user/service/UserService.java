package org.user.service;


import org.user.exception.UserNotFoundException;
import org.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUserById(long id) throws UserNotFoundException;

    User updateUser(long id, User updatedUser) throws UserNotFoundException;

    void deleteUser(long id) throws UserNotFoundException;
}
