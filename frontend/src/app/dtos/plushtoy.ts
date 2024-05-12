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
  image: any;
}
export class PlushToyCreationDto {
  name: string;
  price: number;
  description: string;
  taxClass: number;
  weight: number;
  color: PlushToyColor;
  size: PlushToySize;
}

export class PlushToyDetailsDto {
  id: number;
  name: string;
  description: string;
  price: number;
  taxClass: number;
  weight: number;
  size: PlushToySize;
  color: PlushToyColor;
}