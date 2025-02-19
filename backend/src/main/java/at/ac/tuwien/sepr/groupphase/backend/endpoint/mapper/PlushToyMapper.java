package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface PlushToyMapper {

    @Named("entityToListDto")
    List<PlushToyListDto> entityToListDto(List<PlushToy> plushToy);

    @Named("plushToyDetailsDtoToEntity")
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "smartContracts", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "attributeDistributions", source = "plushToyDetailsDto.attributesDistributions")
    PlushToy detailsDtoToEntity(PlushToyDetailDto plushToyDetailsDto);

    @Named("entityToDetailDto")
    @Mapping(target = "productCategories", source = "plushToy.productCategories")
    @Mapping(target = "attributesDistributions", source = "plushToy.attributeDistributions")
    PlushToyDetailDto entityToDetailDto(PlushToy plushToy);

    public static Long[] map(List<PlushToy> plushToys) {
        return new Long[0]; // Ignore nested lists of PlushToys
    }
    
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
