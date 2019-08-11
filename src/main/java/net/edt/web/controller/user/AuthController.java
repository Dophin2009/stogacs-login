package net.edt.web.controller.user;

import net.edt.persistence.domain.AuthToken;
import net.edt.persistence.domain.User;
import net.edt.persistence.service.UserService;
import net.edt.web.converter.AuthTokenDtoConverter;
import net.edt.web.converter.RegistrationToUserConverter;
import net.edt.web.converter.UserDtoConverter;
import net.edt.web.dto.AuthTokenDto;
import net.edt.web.dto.RegistrationContext;
import net.edt.web.dto.RegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDtoConverter userDtoConverter;

    @Autowired
    private AuthTokenDtoConverter authTokenDtoConverter;

    @Autowired
    private RegistrationToUserConverter registrationToUserConverter;

    @PostMapping("/token")
    public AuthTokenDto createAuthToken(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getFromEmail(email);
        AuthToken existingToken = user.getAuthToken();
        if (existingToken != null && !existingToken.isExpired()) {
            return authTokenDtoConverter.convertToDto(existingToken);
        }

        AuthToken token = userService.createToken(user.getId(), 30);
        return authTokenDtoConverter.convertToDto(token);
    }

    @PostMapping("/register")
    public RegistrationResponse registerUser(@Valid @RequestBody RegistrationContext context) {
        User converted = registrationToUserConverter.convertToUser(context);

        userService.create(converted);
        RegistrationResponse response = new RegistrationResponse();
        response.setMessage("Success! Request /user/auth/token to get access token");
        return response;
    }

}
