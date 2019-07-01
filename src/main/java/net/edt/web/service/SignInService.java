package net.edt.web.service;

import net.edt.web.domain.Meeting;
import net.edt.web.domain.SignInRequest;
import net.edt.web.domain.SignInSession;
import net.edt.web.domain.User;
import net.edt.web.exception.EntityNotFoundException;
import net.edt.web.repository.MeetingRepository;
import net.edt.web.repository.SignInRequestRepository;
import net.edt.web.repository.SignInSessionRepository;
import net.edt.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
