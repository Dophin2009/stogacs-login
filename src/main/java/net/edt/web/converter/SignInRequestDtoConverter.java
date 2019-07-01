package net.edt.web.converter;

import net.edt.persistence.domain.SignInRequest;
import net.edt.persistence.domain.SignInSession;
import net.edt.persistence.domain.User;
import net.edt.web.dto.SignInRequestDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SignInRequestDtoConverter implements DtoConverter<SignInRequest, SignInRequestDto> {

    private ModelMapper modelMapper;

    @Autowired
    public SignInRequestDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        TypeMap<SignInRequest, SignInRequestDto> toDtoMap = modelMapper
                .createTypeMap(SignInRequest.class, SignInRequestDto.class);
        Converter<User, String> userToId = ctx -> {
            User user = ctx.getSource();
            return user.getId().toString();
        };
        Converter<SignInSession, String> sessionToId = ctx -> {
            SignInSession session = ctx.getSource();
            return session.getId().toString();
        };
        toDtoMap.addMappings(
                mapper -> {
                    mapper.using(userToId).map(SignInRequest::getUser, SignInRequestDto::setUserId);
                    mapper.using(sessionToId).map(SignInRequest::getSession, SignInRequestDto::setSessionId);
                });

        TypeMap<SignInRequestDto, SignInRequest> toEntityMap = modelMapper
                .createTypeMap(SignInRequestDto.class, SignInRequest.class);
        Converter<String, User> idToUser = ctx -> {
            User user = new User();
            user.setId(UUID.fromString(ctx.getSource()));
            return user;
        };
        Converter<String, SignInSession> idToSession = ctx -> {
            SignInSession session = new SignInSession();
            session.setId(UUID.fromString(ctx.getSource()));
            return session;
        };
        toEntityMap.addMappings(
                mapper -> {
                    mapper.using(idToUser)
                            .map(SignInRequestDto::getUserId, SignInRequest::setUser);
                    mapper.using(idToSession).map(SignInRequestDto::getSessionId, SignInRequest::setSession);
                });
    }

    @Override
    public SignInRequest convertToEntity(SignInRequestDto dto) {
        return modelMapper.map(dto, SignInRequest.class);
    }

    @Override
    public SignInRequestDto convertToDto(SignInRequest entity) {
        return modelMapper.map(entity, SignInRequestDto.class);
    }

}
