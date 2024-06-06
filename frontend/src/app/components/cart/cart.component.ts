import {Component, OnInit} from '@angular/core';
import {PlushToyCartListDto, PlushToyListDto} from "../../dtos/plushtoy";
import {PlushtoyService} from "../../services/plushtoy.service";
import {ShoppingCartService} from "../../services/shopping-cart.service";
import {ToastrService} from "ngx-toastr";
import {NgForOf} from "@angular/common";

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
  cartItems: PlushToyCartListDto[] = [];
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

  increaseAmount(itemId: number): void {
    this.shoppingCartService.addToCart(itemId).subscribe({
      next: () => {
        console.log('Item amount increased successfully');
        this.updateCartItemAmount(itemId, 1);
      },
      error: (error) => {
        console.error('Error occurred while increasing item amount:', error);
        this.notification.error("Cannot increase item amount in the cart");
      }
    });
  }

  decreaseAmount(itemId: number): void {
    this.shoppingCartService.decreaseAmount(itemId).subscribe({
      next: () => {
        console.log('Item amount decreased successfully');
        this.updateCartItemAmount(itemId, -1);
      },
      error: (error) => {
        console.error('Error occurred while decreasing item amount:', error);
        this.notification.error("Cannot decrease item amount in the cart");
      }
    });
  }

  updateCartItemAmount(itemId: number, change: number): void {
    const item = this.cartItems.find(cartItem => cartItem.id === itemId);
    if (item) {
      item.amount += change;
      if (item.amount <= 0) {
        this.cartItems = this.cartItems.filter(cartItem => cartItem.id !== itemId);
      }
      this.calculateTotalPrice();
    }
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
      next: (items: PlushToyCartListDto[]) => {
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
    const total = this.cartItems.reduce((acc, item) => acc + item.price * item.amount, 0);
    this.totalPrice = `${total} SOL`;
  }


}
