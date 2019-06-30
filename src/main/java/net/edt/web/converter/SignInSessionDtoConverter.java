package net.edt.web.converter;

import net.edt.web.domain.SignInSession;
import net.edt.web.transfer.SignInSessionDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignInSessionDtoConverter implements DtoConverter<SignInSession, SignInSessionDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SignInSessionDto convertToDto(SignInSession session) {
        return modelMapper.map(session, SignInSessionDto.class);
    }

    @Override
    public SignInSession convertToEntity(SignInSessionDto sessionDto) {
        return modelMapper.map(sessionDto, SignInSession.class);
    }

}
