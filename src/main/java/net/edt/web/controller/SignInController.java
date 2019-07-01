package net.edt.web.controller;

import net.edt.web.converter.SignInRequestDtoConverter;
import net.edt.web.converter.SignInSessionDtoConverter;
import net.edt.persistence.domain.SignInRequest;
import net.edt.persistence.domain.SignInSession;
import net.edt.web.service.SignInService;
import net.edt.web.dto.SignInRequestDto;
import net.edt.web.dto.SignInSessionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SignInController {

    @Autowired
    private SignInService signInService;

    @Autowired
    private SignInSessionDtoConverter signInSessionDtoConverter;

    @Autowired
    private SignInRequestDtoConverter signInRequestDtoConverter;

    @GetMapping("/admin/signin/sessions")
    public List<SignInSessionDto> retrieveAllSessions() {
        List<SignInSession> sessions = signInService.getAllSessions();
        return sessions.stream()
                .map(signInSessionDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/signin/requests")
    public List<SignInRequestDto> retrieveAllRequests() {
        List<SignInRequest> requests = signInService.getAllRequests();
        return requests.stream()
                .map(signInRequestDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/admin/signin/sessions")
    public SignInSessionDto createSession(@Valid @RequestBody SignInSessionDto sessionDto) {
        sessionDto.setId(null);
        SignInSession converted = signInSessionDtoConverter.convertToEntity(sessionDto);
        SignInSession session = signInService.create(converted);
        return signInSessionDtoConverter.convertToDto(session);
    }

    @PutMapping("/user/signin")
    public SignInRequestDto signIn(@RequestBody SignInRequestDto requestDto) {
        requestDto.setId(null);
        SignInRequest converted = signInRequestDtoConverter.convertToEntity(requestDto);
        SignInRequest request = signInService.create(converted);
        return signInRequestDtoConverter.convertToDto(request);
    }

}
