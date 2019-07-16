package net.edt.web.controller;

import net.edt.persistence.domain.SignInSession;
import net.edt.persistence.repository.SignInSessionRepository;
import net.edt.web.exception.EntityNotFoundException;
import net.edt.web.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/admin/qr")
public class QRController {

    @Autowired
    private QRService qrService;

    @Autowired
    private SignInSessionRepository signInSessionRepository;

    @GetMapping(value = "/{sessionId}:{timecode}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> produceQRCode(@PathVariable String sessionId, @PathVariable String timecode) {
        Optional<SignInSession> foundSession = signInSessionRepository.findById(sessionId);
        if (!foundSession.isPresent()) {
            throw new EntityNotFoundException("SignInSession with id '" + sessionId + "' not found");
        }

        SignInSession session = foundSession.get();
        if (session.getSessionCodes().stream().noneMatch(s -> s.getCode().equals(timecode))) {
            throw new EntityNotFoundException("Invalid timecode '" + timecode + "'");
        }

        byte[] qr = qrService.generateQRCode(sessionId + ":" + timecode);
        return new ResponseEntity<>(qr, HttpStatus.OK);
    }

}
