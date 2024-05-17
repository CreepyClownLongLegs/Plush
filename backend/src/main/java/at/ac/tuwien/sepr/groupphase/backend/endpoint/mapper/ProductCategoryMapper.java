package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryCreationDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ProductCategory;

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
    
}