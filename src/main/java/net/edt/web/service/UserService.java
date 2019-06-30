package net.edt.web.service;

import net.edt.web.domain.Meeting;
import net.edt.web.domain.User;
import net.edt.web.exception.EntityNotFoundException;
import net.edt.web.repository.MeetingRepository;
import net.edt.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getFromId(UUID id) {
        Optional<User> found = userRepository.findById(id);
        if (!found.isPresent()) {
            throw createUserNotFoundException(id);
        }
        return found.get();
    }

    public User create(User user) {
        updateMeetings(user);
        return userRepository.save(user);
    }

    public User update(UUID id, User user) {
        Optional<User> foundOptional = userRepository.findById(id);
        if (!foundOptional.isPresent()) {
            throw createUserNotFoundException(id);
        }

        user.setId(id);
        updateMeetings(user);
        return userRepository.save(user);
    }

    public User remove(UUID id) {
        Optional<User> found = userRepository.findById(id);
        if (!found.isPresent()) {
            throw createUserNotFoundException(id);
        }
        userRepository.delete(found.get());
        return found.get();
    }

    private void updateMeetings(User user) {
        Set<Meeting> meetings = user.getMeetings();

        Set<Meeting> replace = new HashSet<>();
        for (Meeting meeting : meetings) {
            if (meeting.getId() != null) {
                Optional<Meeting> foundMeeting = meetingRepository.findById(meeting.getId());
                if (!foundMeeting.isPresent()) {
                    throw new EntityNotFoundException("Meeting with id '" + meeting.getId() + "' not found");
                }

                meetings.remove(meeting);
                replace.add(foundMeeting.get());
            }
        }
        user.setMeetings(replace);
    }

    private EntityNotFoundException createUserNotFoundException(UUID id) {
        return new EntityNotFoundException("User with id '" + id + "' not found");
    }

}
