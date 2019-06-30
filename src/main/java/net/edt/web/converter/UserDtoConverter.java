package net.edt.web.converter;

import net.edt.web.domain.User;
import net.edt.web.transfer.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter implements DtoConverter<User, UserDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User convertToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

}
