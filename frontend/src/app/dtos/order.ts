export class OrderListDto {
  id: number;
  totalPrice: number;
  timestamp: Date;
  totalTax: number;
  orderItems: OrderItem[];
  deliveryStatus: string;
}

export class OrderItem {
  id: number;
  pricePerPiece: number;
  plushToyId: number;
  name: string;
  amount: number;
  imageUrl: string;
}

export class OrderDetailDto {
  //IMPL
  id: number;
  totalPrice: number;
  timestamp: Date;
  totalTax: number;
  orderItems: OrderItem[];
}

export class OrderCreateDto {
  signature: string
}
