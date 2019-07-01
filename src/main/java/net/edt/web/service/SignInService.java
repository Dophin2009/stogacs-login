package net.edt.web.service;

import net.edt.persistence.domain.Meeting;
import net.edt.persistence.domain.SignInRequest;
import net.edt.persistence.domain.SignInSession;
import net.edt.persistence.domain.User;
import net.edt.web.exception.EntityNotFoundException;
import net.edt.persistence.repository.MeetingRepository;
import net.edt.persistence.repository.SignInRequestRepository;
import net.edt.persistence.repository.SignInSessionRepository;
import net.edt.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SignInService {

    @Autowired
    private SignInSessionRepository signInSessionRepository;

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
        return signInSessionRepository.save(session);
    }

    public SignInRequest create(SignInRequest request) {
        replaceUser(request);
        replaceSession(request);
        request.setTime(LocalDateTime.now());

        LocalDateTime requestTime = request.getTime();
        SignInSession session = request.getSession();
        boolean requestSuccess = requestTime.isAfter(session.getStartTime()) && requestTime.isBefore(session.getEndTime());
        request.setSuccess(requestSuccess);

        return signInRequestRepository.save(request);
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
        UUID sessionID = request.getSession().getId();
        Optional<SignInSession> foundSession = signInSessionRepository.findById(sessionID);
        if (!foundSession.isPresent()) {
            throw new EntityNotFoundException("Session with id '" + sessionID + "' not found");
        }
        request.setSession(foundSession.get());
    }

}
