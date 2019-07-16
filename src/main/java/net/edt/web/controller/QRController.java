package net.edt.web.controller;

import net.edt.persistence.domain.SignInSession;
import net.edt.persistence.domain.SignInSessionCode;
import net.edt.persistence.repository.SignInSessionRepository;
import net.edt.web.exception.EntityNotFoundException;
import net.edt.web.exception.InvalidFormatException;
import net.edt.web.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Comparator;
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
        SignInSession session = findSession(sessionId);
        if (session.getSessionCodes().stream().noneMatch(s -> s.getCode().equals(timecode))) {
            throw new EntityNotFoundException("Invalid timecode '" + timecode + "'");
        }

        byte[] qr = qrService.generateQRCode(constructInstanceFullIdentifier(sessionId, timecode));
        return new ResponseEntity<>(qr, HttpStatus.OK);
    }

    @GetMapping(value = "/{sessionId}:current", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> produceQRCode(@PathVariable String sessionId) {
        SignInSession session = findSession(sessionId);

        LocalDateTime now = LocalDateTime.now();
        if (!now.isAfter(session.getStartTime()) || !now.isBefore(session.getEndTime())) {
            throw new InvalidFormatException("SignInSession with id '" + sessionId + "' is not currently active");
        }

        Comparator<SignInSessionCode> signInSessionCodeComparator = (sc1, sc2) -> {
            LocalDateTime startTime1 = sc1.getStartTime(), startTime2 = sc2.getStartTime();
            if (startTime1.isAfter(startTime2)) {
                return -1;
            } else if (startTime1.isBefore(startTime2)) {
                return 1;
            }

            LocalDateTime endTime1 = sc1.getEndTime(), endTime2 = sc2.getEndTime();
            if (endTime1.isAfter(endTime2)) {
                return -1;
            } else if (endTime1.isBefore(endTime2)) {
                return 1;
            }
            return 0;
        };
        SignInSessionCode code =
                session.getSessionCodes().stream()
                       .filter(sc -> sc != null && now.isAfter(sc.getStartTime()) && now.isBefore(sc.getEndTime()))
                       .min(signInSessionCodeComparator)
                       .get();

        byte[] qr = qrService.generateQRCode(constructInstanceFullIdentifier(sessionId, code.getCode()));
        return new ResponseEntity<>(qr, HttpStatus.OK);
    }

    private String constructInstanceFullIdentifier(String sessionId, String timecode) {
        return sessionId + ":" + timecode;
    }

    private SignInSession findSession(String id) {
        Optional<SignInSession> foundSession = signInSessionRepository.findById(id);
        if (!foundSession.isPresent()) {
            throw new EntityNotFoundException("SignInSession with id '" + id + "' not found");
        }
        return foundSession.get();
    }

}
