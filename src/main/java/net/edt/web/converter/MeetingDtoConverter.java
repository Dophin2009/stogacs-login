package net.edt.web.converter;

import net.edt.web.domain.Meeting;
import net.edt.web.transfer.MeetingDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MeetingDtoConverter implements DtoConverter<Meeting, MeetingDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MeetingDto convertToDto(Meeting meeting) {
        return modelMapper.map(meeting, MeetingDto.class);
    }

    @Override
    public Meeting convertToEntity(MeetingDto meetingDto) {
        return modelMapper.map(meetingDto, Meeting.class);
    }

}
