package net.edt.web.converter;

import net.edt.persistence.domain.AuthToken;
import net.edt.web.dto.AuthTokenDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenDtoConverter implements DtoConverter<AuthToken, AuthTokenDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AuthToken convertToEntity(AuthTokenDto tokenDto) {
        return modelMapper.map(tokenDto, AuthToken.class);
    }

    @Override
    public AuthTokenDto convertToDto(AuthToken token) {
        return modelMapper.map(token, AuthTokenDto.class);
    }

}
