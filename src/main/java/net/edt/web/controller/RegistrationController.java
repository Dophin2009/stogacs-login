package net.edt.web.controller;

import net.edt.web.converter.RegistrationToUserConverter;
import net.edt.web.converter.UserDtoConverter;
import net.edt.persistence.domain.User;
import net.edt.web.service.UserService;
import net.edt.web.dto.RegistrationContext;
import net.edt.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDtoConverter userDtoConverter;

    @Autowired
    private RegistrationToUserConverter registrationToUserConverter;

    @PostMapping("/user/register")
    public UserDto registerUser(@Valid @RequestBody RegistrationContext context) {
        User converted = registrationToUserConverter.convertToUser(context);
        User newUser = userService.create(converted);
        return userDtoConverter.convertToDto(newUser);
    }

}
