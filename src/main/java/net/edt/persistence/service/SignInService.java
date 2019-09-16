package net.edt.persistence.service;

import net.edt.persistence.domain.*;
import net.edt.persistence.repository.*;
import net.edt.web.exception.EntityNotFoundException;
import net.edt.web.exception.InvalidFormatException;
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

    public SignInSession getSessionFromId(String id) {
        Optional<SignInSession> foundSession = signInSessionRepository.findById(id);
        if (!foundSession.isPresent()) {
            throw new EntityNotFoundException("SignInSession with id '" + id + "' not found");
        }
        return foundSession.get();
    }

    public SignInSessionCode getCurrentCode(String id) {
        SignInSession session = getSessionFromId(id);
        LocalDateTime now = LocalDateTime.now();
        if (!now.isAfter(session.getStartTime()) || !now.isBefore(session.getEndTime())) {
            throw new InvalidFormatException("SignInSession with id '" + id + "' is not currently active");
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
        Optional<SignInSessionCode> code =
                session.getSessionCodes().stream()
                       .filter(sc -> sc != null && now.isAfter(sc.getStartTime()) && now.isBefore(sc.getEndTime()))
                       .min(signInSessionCodeComparator);
        if (!code.isPresent()) {
            throw new InvalidFormatException("No codes are currently active for the session with id '" + id + "'");
        }
        return code.get();
    }

    public List<SignInRequest> getAllRequests() {
        return signInRequestRepository.findAll();
    }

    public SignInRequest getRequestFromId(String id) {
        Optional<SignInRequest> foundRequest = signInRequestRepository.findById(id);
        if (!foundRequest.isPresent()) {
            throw new EntityNotFoundException("SignInRequest with id '" + id + "' not found");
        }
        return foundRequest.get();
    }

    public SignInSession create(SignInSession session) {
        replaceMeeting(session);
        replaceSignInRequests(session.getSignInRequests());

        // if no session codes are provided, generate them
        int codeRefresh = session.getCodeRefresh();
        if ((session.getSessionCodes() == null || session.getSessionCodes().isEmpty()) && codeRefresh > 0) {
            Set<SignInSessionCode> codes = generateSessionCodes(session);
            session.setSessionCodes(codes);
        } else if (codeRefresh == 0) {
            LocalDateTime codeEndTime = session.getEndTime().plusSeconds(session.getCodeRefreshOffset());
            SignInSessionCode singleCode = new SignInSessionCode(session.getStartTime(), codeEndTime);
            Set<SignInSessionCode> codes = new HashSet<>();
            codes.add(singleCode);

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
                throw new EntityNotFoundException("Time code '" + timecode + "' not found");
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
            SignInSessionCode newCode = new SignInSessionCode(time, time.plusSeconds(
                    refreshRate + session.getCodeRefreshOffset()));
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
