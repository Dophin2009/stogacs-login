package net.edt.web.converter;

import net.edt.web.domain.SignInRequest;
import net.edt.web.transfer.SignInRequestDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SignInRequestDtoConverter implements DtoConverter<SignInRequest, SignInRequestDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SignInRequest convertToEntity(SignInRequestDto dto) {
        SignInRequest request = modelMapper.map(dto, SignInRequest.class);
        request.getUser().setId(UUID.fromString(dto.getUser().getId()));
        request.getSession().setId(UUID.fromString(dto.getSession().getId()));
        return request;
    }

    @Override
    public SignInRequestDto convertToDto(SignInRequest entity) {
        return modelMapper.map(entity, SignInRequestDto.class);
    }

}
