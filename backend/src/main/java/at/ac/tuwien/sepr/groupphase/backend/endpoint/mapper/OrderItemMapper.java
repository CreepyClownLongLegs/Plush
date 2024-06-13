package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderItemListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.OrderItem;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    @Named("entityToDto")
    @Mapping(target = "plushToyId", source = "plushToy.id")
    OrderItemListDto entityToDto(OrderItem item);

    @Named("entityToDtoList")
    @IterableMapping(qualifiedByName = "entityToDto")
    List<OrderItemListDto> entityToDtoList(List<OrderItem> orderItems);
}
