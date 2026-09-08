package pl.smyk.gympassbackend.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.smyk.gympassbackend.member.dto.MembershipDto;
import pl.smyk.gympassbackend.member.model.Membership;

@Mapper(componentModel = "spring")
public interface MembershipMapper {
    MembershipMapper INSTANCE = Mappers.getMapper(MembershipMapper.class);

    MembershipDto toDto(Membership membership);

    Membership toEntity(MembershipDto dto);
}
