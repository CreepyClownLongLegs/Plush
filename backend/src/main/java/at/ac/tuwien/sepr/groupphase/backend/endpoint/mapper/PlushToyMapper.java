package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;

@Mapper
public interface PlushToyMapper {

    @Named("entityToListDto")
    List<PlushToyListDto> entityToListDto(List<PlushToy> plushToy);

    @Named("plushToyDetailsDtoToEntity")
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "smartContracts", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "attributeDistributions", ignore = true)
    @Mapping(target = "plushToyAttributes", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    PlushToy detailsDtoToEntity(PlushToyDetailDto plushToyDetailsDto);

    @Named("entityToDetailDto")
    @Mapping(target = "productCategories", source = "plushToy.productCategories")
    PlushToyDetailDto entityToDetailDto(PlushToy plushToy);

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
    UserDetailDto toDto(User user);

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
    User toEntity(UserDetailDto dto);

    @Named("entityListToDtoList")
    List<UserDetailDto> entityListToDtoList(List<User> users);
}
