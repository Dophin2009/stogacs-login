package net.edt.web.controller.admin;

import net.edt.persistence.domain.SignInSession;
import net.edt.persistence.domain.SignInSessionCode;
import net.edt.persistence.service.QRService;
import net.edt.persistence.service.SignInService;
import net.edt.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/qr")
public class QRController {

    @Autowired
    private QRService qrService;

    @Autowired
    private SignInService signInService;

    @GetMapping(value = "/{sessionId}:{timecode}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> produceQRCode(@PathVariable String sessionId, @PathVariable String timecode) {
        SignInSession session = signInService.getSessionFromId(sessionId);
        if (session.getSessionCodes().stream().noneMatch(s -> s.getCode().equals(timecode))) {
            throw new EntityNotFoundException("Invalid timecode '" + timecode + "'");
        }

        byte[] qr = qrService.generateQRCode(constructInstanceFullIdentifier(sessionId, timecode));
        return new ResponseEntity<>(qr, HttpStatus.OK);
    }

    @GetMapping(value = "/{sessionId}:current", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> produceQRCode(@PathVariable String sessionId) {
        SignInSessionCode code = signInService.getCurrentCode(sessionId);
        byte[] qr = qrService.generateQRCode(constructInstanceFullIdentifier(sessionId, code.getCode()));
        return new ResponseEntity<>(qr, HttpStatus.OK);
    }

    private String constructInstanceFullIdentifier(String sessionId, String timecode) {
        return sessionId + ":" + timecode;
    }

}
