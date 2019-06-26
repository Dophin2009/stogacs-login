package net.edt.web.service;

import net.edt.web.domain.Meeting;
import net.edt.web.domain.User;
import net.edt.web.exception.EntityNotFoundException;
import net.edt.web.repository.MeetingRepository;
import net.edt.web.repository.UserRepository;
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
    private UserRepository userRepository;

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
        updateUsers(meeting);
        return meetingRepository.save(meeting);
    }

    private void updateUsers(Meeting meeting) {
        Set<User> users = meeting.getUsers();

        Set<User> replace = new HashSet<>();
        for (User user : users) {
            if (user.getId() != null) {
                Optional<User> foundUser = userRepository.findById(user.getId());
                if (foundUser.isPresent()) {
                    users.remove(user);
                    replace.add(foundUser.get());
                } else {
                    throw new EntityNotFoundException("User with id '" + user.getId() + "' not found");
                }
            }
        }
        meeting.setUsers(replace);
    }

    private EntityNotFoundException createMeetingNotFoundException(Long id) {
        return new EntityNotFoundException("Meeting with id '" + id + "' not found");
    }

}
