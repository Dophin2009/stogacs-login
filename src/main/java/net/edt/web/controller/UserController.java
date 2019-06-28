package net.edt.web.controller;

import net.edt.web.domain.User;
import net.edt.web.exception.InvalidFormatException;
import net.edt.web.service.UserService;
import net.edt.web.transfer.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<UserDto> retrieveAllUsers() {
        List<User> users = userService.getAll();
        return users.stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto retrieveUser(@PathVariable(name = "id") String id) {
        try {
            User user = userService.getFromId(UUID.fromString(id));
            return convertToDto(user);
        } catch (IllegalArgumentException ex) {
            throw createInvalidUserIDException(id);
        }
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        User converted = convertToEntity(userDto);
        User newUser = userService.create(converted);
        return convertToDto(newUser);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable(name = "id") String id, @Valid @RequestBody UserDto userDto) {
        try {
            User toPut = convertToEntity(userDto);
            User updated = userService.update(UUID.fromString(id), toPut);
            return convertToDto(updated);
        } catch (IllegalArgumentException ex) {
            throw createInvalidUserIDException(id);
        }
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable(name = "id") String id) {
        try {
            User removed = userService.remove(UUID.fromString(id));
            return convertToDto(removed);
        } catch (IllegalArgumentException ex) {
            throw createInvalidUserIDException(id);
        }
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User convertToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private InvalidFormatException createInvalidUserIDException(String id) {
        return new InvalidFormatException("Invalid UUID '" + id + "' in request");
    }

}
