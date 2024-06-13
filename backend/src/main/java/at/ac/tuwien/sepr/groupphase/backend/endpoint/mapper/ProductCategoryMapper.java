package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryCreationDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ProductCategory;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ProductCategoryMapper {
    @Named("creationDtoToEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "plushToys", ignore = true)
    ProductCategory creationDtoToEntity(ProductCategoryCreationDto productCategoryDto);

    @Named("entityToDto")
    ProductCategoryDto entityToDto(ProductCategory productCategory);

    @Named("entityListToDtoList")
    List<ProductCategoryDto> entityListToDtoList(List<ProductCategory> productCategories);

    @Named("detailDtoListToIdList")
    @IterableMapping(qualifiedByName = "dtoToId")
    List<Long> productCategoryDtoListToIdList(List<ProductCategoryDto> productCategoryDtos);

    @Named("dtoListToEntityList")
    List<ProductCategory> productCategoryDtoListToEntityList(List<ProductCategoryDto> productCategoryDtoList);

    @Named("dtoToId")
    default Long dtoToId(ProductCategoryDto productCategoryDto) {
        return productCategoryDto.getId();
    }
}