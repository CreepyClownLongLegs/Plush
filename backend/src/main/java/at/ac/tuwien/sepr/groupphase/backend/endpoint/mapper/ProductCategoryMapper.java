package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryCreationDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
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

    public static Long[] map(List<PlushToy> plushToys) {
        if (plushToys == null) {
            return new Long[0];
        }
        return plushToys.stream()
            .map(PlushToy::getId)
            .toArray(Long[]::new);
    }
    
    @Named("detailDtoListToIdList")
    @IterableMapping(qualifiedByName = "dtoToId")
    List<Long> productCategoryDtoListToIdList(List<ProductCategoryDto> productCategoryDtos);

    /*@Named("dtoListToEntityList")
    List<ProductCategory> productCategoryDtoListToEntityList(List<ProductCategoryDto> productCategoryDtoList);
*/
    @Named("dtoToId")
    default Long dtoToId(ProductCategoryDto productCategoryDto) {
        return productCategoryDto.getId();
    }
}