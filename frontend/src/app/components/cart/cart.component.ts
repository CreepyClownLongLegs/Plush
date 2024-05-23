import {Component, OnInit} from '@angular/core';
import {PlushToyListDto} from "../../dtos/plushtoy";
import {PlushtoyService} from "../../services/plushtoy.service";
import {ShoppingCartService} from "../../services/shopping-cart.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {ToastrService} from "ngx-toastr";
import {NgForOf} from "@angular/common";
import {jwtDecode} from "jwt-decode";

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent implements OnInit{
  cartItems: PlushToyListDto[] = [];
  totalPrice: string;
  selectedPaymentMethod: string;

  constructor(
    private service: PlushtoyService,
    private shoppingCartService: ShoppingCartService,
    private notification: ToastrService,
  ) {
  }

  removeItem(itemId: number): void {
    this.shoppingCartService.deleteFromCart(itemId).subscribe({
      next: () => {
        console.log('Item removed successfully');
        this.loadCart(); // Reload the cart after successful removal
      },
      error: (error) => {
        console.error('Error occurred while removing item:', error);
        this.notification.error("Cannot remove item from the cart");
      }
    });
  }


  finishPayment(): void {
    if (!this.selectedPaymentMethod) {
      console.error('Please select a payment method');
      return;
    }

    console.log('Finishing payment with method:', this.selectedPaymentMethod);
  }

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.shoppingCartService.getFullCart().subscribe({
      next: (items: PlushToyListDto[]) => {
        this.cartItems = items;
        this.calculateTotalPrice();
      },
      error: (error) => {
        console.error('Error loading cart items', error);
      }
    });
  }

  selectPaymentMethod(paymentMethod: string): void {
    this.selectedPaymentMethod = paymentMethod;
  }

  calculateTotalPrice(): void {
    const total = this.cartItems.reduce((acc, item) => acc + item.price, 0);
    this.totalPrice = `${total} SOL`;
  }


}
