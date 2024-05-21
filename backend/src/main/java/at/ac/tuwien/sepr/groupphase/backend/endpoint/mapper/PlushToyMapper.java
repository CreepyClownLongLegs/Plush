package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import java.util.List;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyCreationDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailsDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;

@Mapper
public interface PlushToyMapper {

    @Named("entityToListDto")
    List<PlushToyListDto> entityToListDto(List<PlushToy> plushToy);

    @Named("plushToyCreationDtoToEntity")
    PlushToy creationDtoToEntity(PlushToyCreationDto plushToyCreationDto);

    @Named("entityToDetailsDto")
    PlushToyDetailsDto entityToDetailsDto(PlushToy plushToy);

    @Named("entityToDetailDto")
    PlushToyDetailDto entityToDetailDto(PlushToy plushToy);
}
