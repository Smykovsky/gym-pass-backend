package pl.smyk.gympassbackend.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.smyk.gympassbackend.member.dto.MembershipDto;
import pl.smyk.gympassbackend.member.model.Membership;
import pl.smyk.gympassbackend.member.model.MembershipType;

@Mapper(componentModel = "spring")
public interface MembershipMapper {
    MembershipMapper INSTANCE = Mappers.getMapper(MembershipMapper.class);

    @Mapping(source = "membershipType", target = "membershipType", qualifiedByName = "mapMembershipTypeToString")
    MembershipDto toDto(Membership membership);

    @Mapping(source = "membershipType", target = "membershipType", qualifiedByName = "mapStringToMembershipType")
    Membership toEntity(MembershipDto dto);

    @Named("mapMembershipTypeToString")
    default String mapMembershipTypeToString(MembershipType membershipType) {
        return membershipType != null ? membershipType.getValue() : null;
    }

    @Named("mapStringToMembershipType")
    default MembershipType mapStringToMembershipType(String value) {
        if (value == null) return null;
        for (MembershipType type : MembershipType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Nieznany typ członkostwa: " + value);
    }
}