package net.edt.web.converter;

import net.edt.persistence.domain.Meeting;
import net.edt.persistence.domain.SignInSession;
import net.edt.web.dto.MeetingDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MeetingDtoConverter implements DtoConverter<Meeting, MeetingDto> {

    private ModelMapper modelMapper;

    @Autowired
    public MeetingDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        TypeMap<Meeting, MeetingDto> toDtoMap = modelMapper.createTypeMap(Meeting.class, MeetingDto.class);
        Converter<Set<SignInSession>, Set<String>> sessionToId = ctx -> {
            Set<SignInSession> requests = ctx.getSource();
            return requests.stream().map(req -> req.getId().toString()).collect(Collectors.toSet());
        };
        toDtoMap.addMappings(
                mapper -> mapper.using(sessionToId).map(Meeting::getSignInSessions, MeetingDto::setSignInSessionIds));

        TypeMap<MeetingDto, Meeting> toEntityMap = modelMapper.createTypeMap(MeetingDto.class, Meeting.class);
        Converter<Set<String>, Set<SignInSession>> idToSession = ctx -> {
            Set<String> ids = ctx.getSource();
            return ids.stream().map(id -> {
                SignInSession session = new SignInSession();
                session.setId(UUID.fromString(id));
                return session;
            }).collect(Collectors.toSet());
        };
        toEntityMap.addMappings(
                mapper -> mapper.using(idToSession).map(MeetingDto::getSignInSessionIds, Meeting::setSignInSessions));
    }

    @Override
    public MeetingDto convertToDto(Meeting meeting) {
        return modelMapper.map(meeting, MeetingDto.class);
    }

    @Override
    public Meeting convertToEntity(MeetingDto meetingDto) {
        return modelMapper.map(meetingDto, Meeting.class);
    }

}
