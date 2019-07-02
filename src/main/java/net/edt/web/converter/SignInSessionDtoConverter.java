package net.edt.web.converter;

import net.edt.persistence.domain.Meeting;
import net.edt.persistence.domain.SignInRequest;
import net.edt.persistence.domain.SignInSession;
import net.edt.web.dto.SignInSessionDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SignInSessionDtoConverter implements DtoConverter<SignInSession, SignInSessionDto> {

    private ModelMapper modelMapper;

    @Autowired
    public SignInSessionDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        TypeMap<SignInSession, SignInSessionDto> toDtoMap = modelMapper
                .createTypeMap(SignInSession.class, SignInSessionDto.class);
        Converter<Set<SignInRequest>, Set<String>> requestToId = ctx -> {
            Set<SignInRequest> requests = ctx.getSource();
            return requests.stream().map(req -> req.getId().toString()).collect(Collectors.toSet());
        };
        Converter<Meeting, Long> meetingToId = ctx -> {
            Meeting meeting = ctx.getSource();
            return meeting.getId();
        };
        toDtoMap.addMappings(
                mapper -> {
                    mapper.using(requestToId)
                            .map(SignInSession::getSignInRequests, SignInSessionDto::setSignInRequestIds);
                    mapper.using(meetingToId).map(SignInSession::getMeeting, SignInSessionDto::setMeetingId);
                });

        TypeMap<SignInSessionDto, SignInSession> toEntityMap = modelMapper
                .createTypeMap(SignInSessionDto.class, SignInSession.class);
        Converter<Set<String>, Set<SignInRequest>> idToRequest = ctx -> {
            Set<String> ids = ctx.getSource();
            return ids.stream().map(id -> {
                SignInRequest request = new SignInRequest();
                request.setId(id);
                return request;
            }).collect(Collectors.toSet());
        };
        Converter<Long, Meeting> idToMeeting = ctx -> {
            Meeting meeting = new Meeting();
            meeting.setId(ctx.getSource());
            return meeting;
        };
        toEntityMap.addMappings(
                mapper -> {
                    mapper.using(idToRequest)
                            .map(SignInSessionDto::getSignInRequestIds, SignInSession::setSignInRequests);
                    mapper.using(idToMeeting).map(SignInSessionDto::getMeetingId, SignInSession::setMeeting);
                });
    }

    @Override
    public SignInSessionDto convertToDto(SignInSession session) {
        return modelMapper.map(session, SignInSessionDto.class);
    }

    @Override
    public SignInSession convertToEntity(SignInSessionDto sessionDto) {
        return modelMapper.map(sessionDto, SignInSession.class);
    }

}
