package net.edt.web.controller;

import net.edt.web.converter.UserDtoConverter;
import net.edt.persistence.domain.User;
import net.edt.web.exception.InvalidFormatException;
import net.edt.persistence.service.UserService;
import net.edt.web.dto.UserDto;
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
    private UserDtoConverter userDtoConverter;

    @GetMapping
    public List<UserDto> retrieveAllUsers() {
        List<User> users = userService.getAll();
        return users.stream().map(userDtoConverter::convertToDto)
                    .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto retrieveUser(@PathVariable(name = "id") String id) {
        try {
            User user = userService.getFromId(UUID.fromString(id));
            return userDtoConverter.convertToDto(user);
        } catch (IllegalArgumentException ex) {
            throw createInvalidUserIDException(id);
        }
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable(name = "id") String id, @Valid @RequestBody UserDto userDto) {
        if (!id.equals(userDto.getId())) {
            throw new InvalidFormatException("Property 'id' may not be changed");
        }

        try {
            User toPut = userDtoConverter.convertToEntity(userDto);
            User updated = userService.update(UUID.fromString(id), toPut);
            return userDtoConverter.convertToDto(updated);
        } catch (IllegalArgumentException ex) {
            throw createInvalidUserIDException(id);
        }
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable(name = "id") String id) {
        try {
            User removed = userService.remove(UUID.fromString(id));
            return userDtoConverter.convertToDto(removed);
        } catch (IllegalArgumentException ex) {
            throw createInvalidUserIDException(id);
        }
    }

    private InvalidFormatException createInvalidUserIDException(String id) {
        return new InvalidFormatException("Invalid UUID '" + id + "' in request");
    }

}
