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
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> retrieveAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User retrieveUser(@PathVariable(name = "id") String id) {
        try {
            return userService.getFromId(UUID.fromString(id));
        } catch (IllegalArgumentException ex) {
            throw createInvalidUserIDException(id);
        }
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable(name = "id") String id, @Valid @RequestBody User user) {
        try {
            return userService.update(UUID.fromString(id), user);
        } catch (IllegalArgumentException ex) {
            throw createInvalidUserIDException(id);
        }
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable(name = "id") String id) {
        try {
            return userService.remove(UUID.fromString(id));
        } catch (IllegalArgumentException ex) {
            throw createInvalidUserIDException(id);
        }
    }

    private InvalidIDException createInvalidUserIDException(String id) {
        return new InvalidIDException("Invalid UUID '" + id + "' in request");
    }

}
