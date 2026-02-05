package org.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.user.exception.UserNotFoundException;
import org.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserServiceTest {

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new InMemoryUserService();
    }

    @Test
    void createUserSuccess() throws Exception {
        User user = new User(1L, "Alice", "alice@test.com");
        User created = service.createUser(user);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Alice", created.getName());
        assertEquals("alice@test.com", created.getEmail());
        User fetched = service.getUserById(1L);
        assertEquals("Alice", fetched.getName());
    }


    @Test
    void createUserNullFailure() {
        assertThrows(NullPointerException.class, () -> service.createUser(null));
    }

    @Test
    void getUserByIdSuccess() throws Exception {
        service.createUser(new User(10L, "Bob", "bob@test.com"));
        User user = service.getUserById(10L);
        assertEquals(10L, user.getId());
        assertEquals("Bob", user.getName());
        assertEquals("bob@test.com", user.getEmail());
    }

    @Test
    void getUserByIdFailure() {
        UserNotFoundException ex = assertThrows(
                UserNotFoundException.class,
                () -> service.getUserById(999L)
        );
        assertTrue(ex.getMessage().contains("999"));
    }

    @Test
    void updateUserSuccess() throws Exception {
        service.createUser(new User(5L, "Old", "old@test.com"));

        User updatedInput = new User(999L, "NewName", "new@test.com");
        User updated = service.updateUser(5L, updatedInput);

        assertEquals(5L, updated.getId());
        assertEquals("NewName", updated.getName());
        assertEquals("new@test.com", updated.getEmail());

        User fetched = service.getUserById(5L);
        assertEquals("NewName", fetched.getName());
        assertEquals("new@test.com", fetched.getEmail());
    }

    @Test
    void updateUserFailure() {
        User updatedInput = new User(1L, "X", "x@test.com");

        UserNotFoundException ex = assertThrows(
                UserNotFoundException.class,
                () -> service.updateUser(123L, updatedInput)
        );

        assertTrue(ex.getMessage().contains("123"));
    }


    @Test
    void deleteUserSuccess() throws Exception {
        service.createUser(new User(7L, "ToDelete", "del@test.com"));

        service.deleteUser(7L);

        assertThrows(UserNotFoundException.class, () -> service.getUserById(7L));
    }

    @Test
    void deleteUserFailure() {
        UserNotFoundException ex = assertThrows(
                UserNotFoundException.class,
                () -> service.deleteUser(404L)
        );

        assertTrue(ex.getMessage().contains("404"));
    }
}
