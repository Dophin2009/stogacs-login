package net.edt.web.controller.user;

import net.edt.persistence.domain.SignInRequest;
import net.edt.persistence.service.SignInService;
import net.edt.web.converter.SignInRequestDtoConverter;
import net.edt.web.dto.SignInRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/signin")
public class SignInController {

    @Autowired
    private SignInService signInService;

    @Autowired
    private SignInRequestDtoConverter signInRequestDtoConverter;

    @PutMapping
    public SignInRequestDto signIn(@RequestBody SignInRequestDto requestDto) {
        requestDto.setId(null);
        SignInRequest converted = signInRequestDtoConverter.convertToEntity(requestDto);
        SignInRequest request = signInService.create(converted);
        return signInRequestDtoConverter.convertToDto(request);
    }

}
