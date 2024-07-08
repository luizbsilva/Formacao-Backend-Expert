package br.com.kayros.mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import br.com.kayros.entities.Order;
import br.com.kayros.model.enums.OrderStatusEnum;
import br.com.kayros.model.request.CreateOrderRequest;
import br.com.kayros.model.request.UpdateOrderRequest;
import br.com.kayros.model.response.OrderResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = IGNORE,
    nullValueCheckStrategy = ALWAYS
)
public interface OrderMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
  @Mapping(target = "createdAt", expression = "java(mapCreatedAt())")
  Order fromRequest(CreateOrderRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "status", source = "request.status", qualifiedByName = "mapStatus")
  Order fromRequest(@MappingTarget Order entity, UpdateOrderRequest request);

  OrderResponse fromEntity(Order save);

  @Named("mapStatus")
  default OrderStatusEnum mapStatus(final String status) {
    return OrderStatusEnum.toEnum(status);
  }

  default LocalDateTime mapCreatedAt() {
    return LocalDateTime.now();
  }

  List<OrderResponse> fromEntities(List<Order> orders);
}
