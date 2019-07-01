package net.edt.web.converter;

import net.edt.web.domain.SignInRequest;
import net.edt.web.domain.User;
import net.edt.web.transfer.UserDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserDtoConverter implements DtoConverter<User, UserDto> {

    private ModelMapper modelMapper;

    @Autowired
    public UserDtoConverter(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        TypeMap<User, UserDto> toDtoMap = modelMapper.createTypeMap(User.class, UserDto.class);
        Converter<Set<SignInRequest>, Set<String>> requestToId = ctx -> {
            Set<SignInRequest> requests = ctx.getSource();
            return requests.stream().map(req -> req.getId().toString()).collect(Collectors.toSet());
        };
        toDtoMap.addMappings(
                mapper -> mapper.using(requestToId).map(User::getSignInRequests, UserDto::setSignInRequestIds));

        TypeMap<UserDto, User> toEntityMap = modelMapper.createTypeMap(UserDto.class, User.class);
        Converter<Set<String>, Set<SignInRequest>> idToRequest = ctx -> {
            Set<String> ids = ctx.getSource();
            return ids.stream().map(id -> {
                SignInRequest request = new SignInRequest();
                request.setId(UUID.fromString(id));
                return request;
            }).collect(Collectors.toSet());
        };
        toEntityMap.addMappings(
                mapper -> mapper.using(idToRequest).map(UserDto::getSignInRequestIds, User::setSignInRequests));
    }

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User convertToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

}
