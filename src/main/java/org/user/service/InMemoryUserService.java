package org.user.service;

import org.user.exception.UserNotFoundException;
import org.user.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryUserService implements UserService {

    private final ConcurrentMap<Long, User> store;
            //= new ConcurrentHashMap<>();

    public InMemoryUserService() {
        this(new ConcurrentHashMap<>());
    }

    public InMemoryUserService(ConcurrentMap<Long, User> store) {
        this.store = Objects.requireNonNull(store, "store must not be null");
    }

    @Override
    public User createUser(User user) {
        Objects.requireNonNull(user, "user must not be null");
        User existing = store.putIfAbsent(user.getId(), user);
        if (existing != null) {
            throw new IllegalArgumentException("User already exists with id: " + user.getId());
        }
        return user;
    }

    @Override
    public User getUserById(long id) throws UserNotFoundException {
        User user = store.get(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }


    @Override
    public User updateUser(long id, User updatedUser) throws UserNotFoundException {
        Objects.requireNonNull(updatedUser, "updatedUser must not be null");

        updatedUser.setId(id);
        getUserById(id);
        User updated = store.computeIfPresent(id, (k, old) -> updatedUser);

        return updated;
    }

    @Override
    public void deleteUser(long id) throws UserNotFoundException {
        getUserById(id);
        store.remove(id);

    }
}
