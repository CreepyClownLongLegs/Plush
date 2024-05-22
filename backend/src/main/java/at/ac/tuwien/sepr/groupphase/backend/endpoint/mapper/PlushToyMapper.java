package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyCreationDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;

@Mapper
public interface PlushToyMapper {

    @Named("entityToListDto")
    List<PlushToyListDto> entityToListDto(List<PlushToy> plushToy);

    @Named("plushToyCreationDtoToEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "smartContract", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "attributeDistributions", ignore = true)
    @Mapping(target = "plushToyAttributes", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    PlushToy creationDtoToEntity(PlushToyCreationDto plushToyCreationDto);

    @Named("entityToDetailDto")
    PlushToyDetailDto entityToDetailDto(PlushToy plushToy);
}
