package net.edt.web.service;

import net.edt.persistence.domain.*;
import net.edt.persistence.repository.*;
import net.edt.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SignInService {

    @Autowired
    private SignInSessionRepository signInSessionRepository;

    @Autowired
    private SignInSessionCodeRepository signInSessionCodeRepository;

    @Autowired
    private SignInRequestRepository signInRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    public List<SignInSession> getAllSessions() {
        return signInSessionRepository.findAll();
    }

    public List<SignInRequest> getAllRequests() {
        return signInRequestRepository.findAll();
    }

    public SignInSession create(SignInSession session) {
        replaceMeeting(session);
        replaceSignInRequests(session.getSignInRequests());

        // if no session codes are provided, generate them
        if (session.getSessionCodes() == null || session.getSessionCodes().isEmpty()) {
            Set<SignInSessionCode> codes = generateSessionCodes(session);
            session.setSessionCodes(codes);
        }

        return signInSessionRepository.save(session);
    }

    public SignInRequest create(SignInRequest request) {
        replaceUser(request);
        replaceSession(request);
        request.setTime(LocalDateTime.now());

        // success validation could probably be simplified significantly
        LocalDateTime requestTime = request.getTime();
        SignInSession session = request.getSession();
        boolean requestSuccess = requestTime.isAfter(session.getStartTime()) && requestTime
                .isBefore(session.getEndTime());

        if (requestSuccess) {
            String timecode = request.getTimecode();
            Optional<SignInSessionCode> foundSessionCode = signInSessionCodeRepository.findById(timecode);
            if (!foundSessionCode.isPresent()) {
                throw new EntityNotFoundException("Time code " + timecode + " not found");
            }
            SignInSessionCode sessionCode = foundSessionCode.get();
            requestSuccess = requestTime.isAfter(sessionCode.getStartTime()) && requestTime
                    .isBefore(sessionCode.getEndTime());
        }

        request.setSuccess(requestSuccess);

        return signInRequestRepository.save(request);
    }

    private Set<SignInSessionCode> generateSessionCodes(SignInSession session) {
        int refreshRate = session.getCodeRefresh();
        LocalDateTime endTime = session.getEndTime();

        Set<SignInSessionCode> codes = new HashSet<>();
        for (LocalDateTime time = session.getStartTime(); time.isBefore(endTime);
             time = time.plusSeconds(refreshRate)) {
            SignInSessionCode newCode = new SignInSessionCode(time, time.plusSeconds(refreshRate + 5));
            codes.add(newCode);
        }
        return codes;
    }

    private void replaceMeeting(SignInSession session) {
        Long meetingId = session.getMeeting().getId();
        Optional<Meeting> foundMeeting = meetingRepository.findById(meetingId);
        if (!foundMeeting.isPresent()) {
            throw new EntityNotFoundException("Meeting with id '" + meetingId + "' not found");
        }
        session.setMeeting(foundMeeting.get());
    }

    private void replaceSignInRequests(Set<SignInRequest> requests) {
        Set<SignInRequest> replacements = new HashSet<>();
        for (SignInRequest req : requests) {
            if (req.getId() != null) {
                Optional<SignInRequest> foundRequest = signInRequestRepository.findById(req.getId());
                if (!foundRequest.isPresent()) {
                    throw new EntityNotFoundException("SignInRequest with id '" + req.getId() + "' not found");
                }

                replacements.add(foundRequest.get());
            }
            requests.remove(req);
        }
        requests.addAll(replacements);
    }

    private void replaceUser(SignInRequest request) {
        UUID userId = request.getUser().getId();
        Optional<User> foundUser = userRepository.findById(userId);
        if (!foundUser.isPresent()) {
            throw new EntityNotFoundException("User with id '" + userId + "' not found");
        }
        request.setUser(foundUser.get());
    }

    private void replaceSession(SignInRequest request) {
        String sessionID = request.getSession().getId();
        Optional<SignInSession> foundSession = signInSessionRepository.findById(sessionID);
        if (!foundSession.isPresent()) {
            throw new EntityNotFoundException("Session with id '" + sessionID + "' not found");
        }
        request.setSession(foundSession.get());
    }

}
