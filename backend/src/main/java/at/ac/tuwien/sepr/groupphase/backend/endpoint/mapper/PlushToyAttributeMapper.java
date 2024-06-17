package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyAttributeDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToyAttribute;

@Mapper
public interface PlushToyAttributeMapper {

    PlushToyAttributeDto entityToDto(PlushToyAttribute value);

    @Mapping(target = "distributions", ignore = true)
    PlushToyAttribute dtoToEntity(PlushToyAttributeDto value);
}
