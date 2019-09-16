package net.edt.web.controller.admin;

import net.edt.persistence.domain.Meeting;
import net.edt.persistence.domain.SignInSession;
import net.edt.persistence.service.MeetingService;
import net.edt.web.converter.MeetingDtoConverter;
import net.edt.web.converter.SignInSessionDtoConverter;
import net.edt.web.dto.MeetingDto;
import net.edt.web.dto.SignInSessionDto;
import net.edt.web.exception.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingDtoConverter meetingDtoConverter;

    @Autowired
    private SignInSessionDtoConverter signInSessionDtoConverter;

    @GetMapping
    public List<MeetingDto> retrieveAllMeetings() {
        List<Meeting> meetings = meetingService.getAll();
        return meetings.stream().map(meetingDtoConverter::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MeetingDto retrieveMeeting(@PathVariable(value = "id") String id) {
        try {
            Meeting meeting = meetingService.getFromId(Long.parseLong(id));
            return meetingDtoConverter.convertToDto(meeting);
        } catch (IllegalArgumentException ex) {
            throw createInvalidMeetingIDException(id);
        }
    }

    @PostMapping
    public MeetingDto createMeeting(@Valid @RequestBody MeetingDto meetingDto) {
        meetingDto.setId(null);
        Meeting converted = meetingDtoConverter.convertToEntity(meetingDto);
        Meeting newMeeting = meetingService.create(converted);
        return meetingDtoConverter.convertToDto(newMeeting);
    }

    @GetMapping("/{id}/sessions")
    public List<SignInSessionDto> retrieveMeetingSessions(@PathVariable(value = "id") String id) {
        try {
            Meeting meeting = meetingService.getFromId(Long.parseLong(id));
            Set<SignInSession> signInSessions = meeting.getSignInSessions();
            return signInSessions.stream().map(signInSessionDtoConverter::convertToDto).collect(Collectors.toList());
        } catch (IllegalArgumentException ex) {
            throw createInvalidMeetingIDException(id);
        }
    }

    private InvalidFormatException createInvalidMeetingIDException(String id) {
        return new InvalidFormatException("Invalid meeting ID '" + id + "' in request");
    }

}
