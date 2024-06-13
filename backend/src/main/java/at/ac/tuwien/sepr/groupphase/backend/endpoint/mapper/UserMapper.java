package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface UserMapper {

    @Named("toDto")
    @Mapping(source = "publicKey", target = "publicKey")
    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "lastname", target = "lastname")
    @Mapping(source = "emailAddress", target = "emailAddress")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "locked", target = "locked")
    @Mapping(source = "country", target = "country")
    @Mapping(source = "postalCode", target = "postalCode")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "addressLine1", target = "addressLine1")
    @Mapping(source = "addressLine2", target = "addressLine2")
    @Mapping(source = "admin", target = "admin")
    UserDetailDto toDto(User user);

    @Named("entityListToDtoList")
    List<UserDetailDto> entityListToDtoList(List<User> users);

    @Named("toListDto")
    @Mapping(source = "publicKey", target = "publicKey")
    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "lastname", target = "lastname")
    @Mapping(source = "admin", target = "admin")
    UserListDto toListDto(User user);

    @Named("entityListToListDtoList")
    List<UserListDto> entityListToListDtoList(List<User> users);

    @Named("toEntity")
    @Mapping(source = "publicKey", target = "publicKey")
    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "lastname", target = "lastname")
    @Mapping(source = "emailAddress", target = "emailAddress")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "locked", target = "locked")
    @Mapping(source = "country", target = "country")
    @Mapping(source = "postalCode", target = "postalCode")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "addressLine1", target = "addressLine1")
    @Mapping(source = "addressLine2", target = "addressLine2")
    @Mapping(source = "admin", target = "admin")
    User toEntity(UserDetailDto dto);

    @Named("toEntity")
    @Mapping(source = "publicKey", target = "publicKey")
    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "lastname", target = "lastname")
    @Mapping(source = "admin", target = "admin")
    User toEntity(UserListDto userListDto);
}