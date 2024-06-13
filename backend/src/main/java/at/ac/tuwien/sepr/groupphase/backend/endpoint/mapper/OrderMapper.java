package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.DeliveryStatus;
import at.ac.tuwien.sepr.groupphase.backend.entity.Order;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Named("entityToDto")
    @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "entityToDtoList")
    @Mapping(target = "deliveryStatus", source = "deliveryStatus", qualifiedBy = {})
    OrderListDto entityToDto(Order order);

    @IterableMapping(qualifiedByName = "entityToDto")
    List<OrderListDto> entityListToDtoList(List<Order> orders);

    default String deliveryStatusToString(DeliveryStatus deliveryStatus) {
        return deliveryStatus != null ? deliveryStatus.getStatus() : null;
    }
}