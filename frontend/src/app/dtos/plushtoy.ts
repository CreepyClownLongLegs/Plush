export enum PlushToyColor {
  RED, GREEN, BLUE, YELLOW, ORANGE, PURPLE, PINK, BROWN, BLACK, WHITE
}

export enum PlushToySize {
  SMALL, MEDIUM, LARGE
}

export class PlushToyListDto {
  id: number;
  name: string;
  price: number;
  averageRating: number;
  description: string;
  imageUrl: string;
  hp: number;
}

export class PlushToyCartListDto {
  id: number;
  name: string;
  price: number;
  averageRating: number;
  description: string;
  imageUrl: string;
  hp: number;
  amount: number;
}

export class PlushToy {
  id?: number;
  name: string;
  price: number;
  description: string;
  taxClass: number;
  weight: number;
  color: PlushToyColor;
  size: PlushToySize;
  hp: number;
  strength: number;
  imageUrl: string;
  productCategories: ProductCategoryDto[];
  attributesDistributions: PlushToyAttributeDistributionDto[] = [];
}

export class ProductCategoryCreationDto {
  name: string;
}

export class ProductCategoryDto {
  id: number;
  name: string;
}

export class PlushToySearchDto {
  name: string;
}

export class PlushToyAttributeDistributionDto {
  id?: number;
  attribute: ProductAttributeDto;
  quantityPercentage: number;
  name: string;
}

export class ProductAttributeDto {
  id?: number;
  name: string;
}

export class PlushToyAttributeDtoWithDistribution {
  id?: number;
  name: string;
  distributions: PlushToyAttributeDistributionDto[];
}


