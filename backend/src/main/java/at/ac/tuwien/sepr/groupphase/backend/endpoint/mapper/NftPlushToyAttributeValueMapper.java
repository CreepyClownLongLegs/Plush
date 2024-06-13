package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana.NftPlushToyAttributeDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.NftPlushToyAttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface NftPlushToyAttributeValueMapper {

    @Named("entityToDto")
    @Mapping(source = "attribute.name", target = "name")
    NftPlushToyAttributeDto entityToDto(NftPlushToyAttributeValue value);

}
