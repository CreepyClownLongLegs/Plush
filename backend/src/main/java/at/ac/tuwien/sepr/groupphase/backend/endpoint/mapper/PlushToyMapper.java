package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;

@Mapper
public interface PlushToyMapper {

    @Named("entityToListDto")
    List<PlushToyListDto> entityToListDto(List<PlushToy> plushToy);
}
