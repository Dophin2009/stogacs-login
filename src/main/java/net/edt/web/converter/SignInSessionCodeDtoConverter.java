package net.edt.web.converter;

import net.edt.persistence.domain.SignInSessionCode;
import net.edt.web.dto.SignInSessionCodeDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignInSessionCodeDtoConverter implements DtoConverter<SignInSessionCode, SignInSessionCodeDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SignInSessionCode convertToEntity(SignInSessionCodeDto dto) {
        return modelMapper.map(dto, SignInSessionCode.class);
    }

    @Override
    public SignInSessionCodeDto convertToDto(SignInSessionCode entity) {
        return modelMapper.map(entity, SignInSessionCodeDto.class);
    }

}
