package net.edt.web.controller;

import net.edt.web.domain.User;
import net.edt.web.exception.InvalidIDException;
import net.edt.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable(name = "id") String id) {
        try {
            return userService.getFromId(UUID.fromString(id));
        } catch (IllegalArgumentException ex) {
            throw createInvalidUserIDException(id);
        }
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable(name = "id") String id, @Valid @RequestBody User user) {
        try {
            return userService.update(UUID.fromString(id), user);
        } catch (IllegalArgumentException ex) {
            throw createInvalidUserIDException(id);
        }
    }

    @DeleteMapping("/users/{id}")
    public User deleteUser(@PathVariable(name = "id") String id) {
        try {
            return userService.remove(UUID.fromString(id));
        } catch (IllegalArgumentException ex) {
            throw createInvalidUserIDException(id);
        }
    }

    private InvalidIDException createInvalidUserIDException(String id) {
        return new InvalidIDException("User with id '" + id + "' not found");
    }

}
