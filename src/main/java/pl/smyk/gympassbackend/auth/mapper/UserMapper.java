package pl.smyk.gympassbackend.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.smyk.gympassbackend.auth.dto.UserDto;
import pl.smyk.gympassbackend.auth.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
