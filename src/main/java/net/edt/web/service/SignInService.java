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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public SignInRequest create(SignInRequest request) {
        UUID userId = request.getUser().getId();
        Optional<User> foundUser = userRepository.findById(userId);
        if (!foundUser.isPresent()) {
            throw new EntityNotFoundException("User with id '" + userId + "' not found");
        }

        UUID sessionID = request.getSession().getId();
        Optional<SignInSession> foundSession = signInSessionRepository.findById(sessionID);
        if (!foundSession.isPresent()) {
            throw new EntityNotFoundException("Session with id '" + sessionID + "' not found");
        }

        request.setUser(foundUser.get());
        request.setSession(foundSession.get());
        return signInRequestRepository.save(request);
    }

    public SignInSession create(SignInSession session) {
        Long meetingId = session.getMeeting().getId();
        Optional<Meeting> foundMeeting = meetingRepository.findById(meetingId);
        if (!foundMeeting.isPresent()) {
            throw new EntityNotFoundException("Meeting with id '" + meetingId + "' not found");
        }

        session.setMeeting(foundMeeting.get());
        return signInSessionRepository.save(session);
    }

}
