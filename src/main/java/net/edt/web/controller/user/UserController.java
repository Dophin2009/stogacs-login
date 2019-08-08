package net.edt.web.controller.user;

import net.edt.persistence.domain.User;
import net.edt.persistence.service.UserService;
import net.edt.web.converter.SignInRequestDtoConverter;
import net.edt.web.converter.UserDtoConverter;
import net.edt.web.dto.SignInRequestDto;
import net.edt.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/self")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDtoConverter userDtoConverter;

    @Autowired
    private SignInRequestDtoConverter signInRequestDtoConverter;

    @GetMapping
    public UserDto getCurrentUser(Authentication authentication) {
        User user = currentUser(authentication);
        return userDtoConverter.convertToDto(user);
    }

    @GetMapping("/requests")
    public List<SignInRequestDto> getCurrentUserSignInRequests(Authentication authentication) {
        User user = currentUser(authentication);
        return user.getSignInRequests()
                   .stream()
                   .map(signInRequestDtoConverter::convertToDto)
                   .collect(Collectors.toList());
    }

    private User currentUser(Authentication authentication) {
        String email = authentication.getName();
        return userService.getFromEmail(email);
    }

}
