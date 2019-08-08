package net.edt.web.controller.admin;

import net.edt.persistence.domain.SignInRequest;
import net.edt.persistence.domain.SignInSession;
import net.edt.persistence.service.SignInService;
import net.edt.web.converter.SignInRequestDtoConverter;
import net.edt.web.converter.SignInSessionDtoConverter;
import net.edt.web.dto.SignInRequestDto;
import net.edt.web.dto.SignInSessionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/signin")
public class AdminSignInController {

    @Autowired
    private SignInService signInService;

    @Autowired
    private SignInSessionDtoConverter signInSessionDtoConverter;

    @Autowired
    private SignInRequestDtoConverter signInRequestDtoConverter;

    @GetMapping("/sessions")
    public List<SignInSessionDto> retrieveAllSessions() {
        List<SignInSession> sessions = signInService.getAllSessions();
        return sessions.stream()
                       .map(signInSessionDtoConverter::convertToDto)
                       .collect(Collectors.toList());
    }

    @GetMapping("/sessions/{id}")
    public SignInSessionDto retrieveSession(@PathVariable(value = "id") String id) {
        SignInSession session = signInService.getSessionFromId(id);
        return signInSessionDtoConverter.convertToDto(session);
    }

    @GetMapping("/requests")
    public List<SignInRequestDto> retrieveAllRequests() {
        List<SignInRequest> requests = signInService.getAllRequests();
        return requests.stream()
                       .map(signInRequestDtoConverter::convertToDto)
                       .collect(Collectors.toList());
    }

    @GetMapping("/requests/{id}")
    public SignInRequestDto retrieveRequest(@PathVariable(value = "id") String id) {
        SignInRequest request = signInService.getRequestFromId(id);
        return signInRequestDtoConverter.convertToDto(request);
    }

    @PostMapping("/sessions")
    public SignInSessionDto createSession(@Valid @RequestBody SignInSessionDto sessionDto) {
        sessionDto.setId(null);
        SignInSession converted = signInSessionDtoConverter.convertToEntity(sessionDto);
        SignInSession session = signInService.create(converted);
        return signInSessionDtoConverter.convertToDto(session);
    }

}
