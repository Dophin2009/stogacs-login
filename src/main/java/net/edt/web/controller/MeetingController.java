package net.edt.web.controller;

import net.edt.web.domain.Meeting;
import net.edt.web.exception.InvalidFormatException;
import net.edt.web.service.MeetingService;
import net.edt.web.transfer.MeetingDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<MeetingDto> retrieveAllMeetings() {
        List<Meeting> meetings = meetingService.getAll();
        return meetings.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MeetingDto retrieveMeeting(@PathVariable(value = "id") String id) {
        try {
            Meeting meeting = meetingService.getFromId(Long.parseLong(id));
            return convertToDto(meeting);
        } catch (IllegalArgumentException ex) {
            throw createInvalidMeetingIDException(id);
        }
    }

    @PostMapping
    public MeetingDto createMeeting(@Valid @RequestBody MeetingDto meetingDto) {
        Meeting converted = convertToEntity(meetingDto);
        Meeting newMeeting = meetingService.create(converted);
        return convertToDto(newMeeting);
    }

    private MeetingDto convertToDto(Meeting meeting) {
        return modelMapper.map(meeting, MeetingDto.class);
    }

    private Meeting convertToEntity(MeetingDto meetingDto) {
        return modelMapper.map(meetingDto, Meeting.class);
    }

    private InvalidFormatException createInvalidMeetingIDException(String id) {
        return new InvalidFormatException("Invalid meeting ID '" + id + "' in request");
    }

}
