package net.edt.web.controller;

import net.edt.web.domain.Meeting;
import net.edt.web.exception.InvalidIDException;
import net.edt.web.service.MeetingService;
import net.edt.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private UserService userService;

    @GetMapping("/meetings")
    public List<Meeting> retrieveAllMeetings() {
        return meetingService.getAll();
    }

    @GetMapping("/meetings/{id}")
    public Meeting retrieveMeeting(@PathVariable(value = "id") String id) {
        try {
            return meetingService.getFromId(Long.parseLong(id));
        } catch (IllegalArgumentException ex) {
            throw createInvalidMeetingIDException(id);
        }
    }

    @PostMapping("/meetings")
    public Meeting createMeeting(@RequestBody Meeting meeting) {
        return meetingService.create(meeting);
    }

    private InvalidIDException createInvalidMeetingIDException(String id) {
        return new InvalidIDException("Meeting with id '" + id + "' not found");
    }

}
