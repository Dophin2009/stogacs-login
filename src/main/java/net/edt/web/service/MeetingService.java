package net.edt.web.service;

import net.edt.persistence.domain.Meeting;
import net.edt.persistence.domain.SignInSession;
import net.edt.web.exception.EntityNotFoundException;
import net.edt.persistence.repository.MeetingRepository;
import net.edt.persistence.repository.SignInSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private SignInSessionRepository signInSessionRepository;

    public List<Meeting> getAll() {
        return meetingRepository.findAll();
    }

    public Meeting getFromId(Long id) {
        Optional<Meeting> found = meetingRepository.findById(id);
        if (!found.isPresent()) {
            throw createMeetingNotFoundException(id);
        }
        return found.get();
    }

    public Meeting create(Meeting meeting) {
        replaceSignInSessions(meeting.getSignInSessions());
        return meetingRepository.save(meeting);
    }

    private void replaceSignInSessions(Set<SignInSession> sessions) {
        Set<SignInSession> replacements = new HashSet<>();
        for (SignInSession session : sessions) {
            if (session.getId() != null) {
                Optional<SignInSession> foundMeeting = signInSessionRepository.findById(session.getId());
                if (!foundMeeting.isPresent()) {
                    throw new EntityNotFoundException("SignInRequest with id '" + session.getId() + "' not found");
                }

                replacements.add(foundMeeting.get());
            }
            sessions.remove(session);
        }
        sessions.addAll(replacements);
    }

    private EntityNotFoundException createMeetingNotFoundException(Long id) {
        return new EntityNotFoundException("Meeting with id '" + id + "' not found");
    }

}
