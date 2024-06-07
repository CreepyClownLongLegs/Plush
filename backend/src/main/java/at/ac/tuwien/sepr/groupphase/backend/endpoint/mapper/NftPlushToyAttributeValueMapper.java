package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana.NftPlushToyAttributeDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.NftPlushToyAttributeValue;

@Mapper
public interface NftPlushToyAttributeValueMapper {

    @Named("entityToDto")
    @Mapping(source = "attribute.name", target = "name")
    NftPlushToyAttributeDto entityToDto(NftPlushToyAttributeValue value);

}
