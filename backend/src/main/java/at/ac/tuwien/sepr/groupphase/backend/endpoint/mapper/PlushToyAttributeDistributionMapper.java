package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyAttributeDistributionDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToyAttributeDistribution;

@Mapper
public interface PlushToyAttributeDistributionMapper {

    @Named("entityToDto")
    PlushToyAttributeDistributionDto entityToDto(PlushToyAttributeDistribution value);

    @Named("dtoToEntity")
    @Mapping(target = "plushToy", ignore = true)
    @Mapping(target = "attribute", source = "value.attribute")
    PlushToyAttributeDistribution dtoToEntity(PlushToyAttributeDistributionDto value);

    @Named("dtoListToEntityList")
    @Mapping(target = "plushToy", ignore = true)
    List<PlushToyAttributeDistribution> dtoListToEntityList(
            List<PlushToyAttributeDistributionDto> productCategoryDtoList);
}
