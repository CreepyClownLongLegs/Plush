import { Component, OnInit } from '@angular/core';
import { PlushToyCartListDto, PlushToyListDto } from "../../dtos/plushtoy";
import { PlushtoyService } from "../../services/plushtoy.service";
import { ShoppingCartService } from "../../services/shopping-cart.service";
import { ToastrService } from "ngx-toastr";
import { NgForOf } from "@angular/common";
import { AuthService } from 'src/app/services/auth.service';
import { WalletService } from 'src/app/services/wallet.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserDetailDto } from 'src/app/dtos/user';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { load } from "@angular-devkit/build-angular/src/utils/server-rendering/esm-in-memory-loader/loader-hooks";

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent implements OnInit {
  cartItems: PlushToyCartListDto[] = [];
  totalPrice: string;
  isCartEmpty: boolean = true;  //  track if the cart is empty

  constructor(
    private service: PlushtoyService,
    private shoppingCartService: ShoppingCartService,
    private notification: ToastrService,
    public authService: AuthService,
    private modalService: NgbModal,
    private userService: UserService,
    private router: Router,
    private walletService: WalletService,
  ) {
  }

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.shoppingCartService.getFullCart().subscribe({
      next: (items: PlushToyCartListDto[]) => {
        this.cartItems = items;
        this.calculateTotalPrice();
        this.isCartEmpty = this.cartItems.length === 0;  // update isCartEmpty
      },
      error: (error) => {
        console.error('Error loading cart items', error);
      }
    });
  }

  calculateTotalPrice(): void {
    const total = this.cartItems.reduce((acc, item) => acc + item.price * item.amount, 0);
    this.totalPrice = `${total} SOL`;
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
      this.isCartEmpty = this.cartItems.length === 0;  // update isCartEmpty
    }
  }

  finishPayment(): void {
    const total: number = this.cartItems.reduce((acc: number, item) => acc + item.price * item.amount, 0);
    if (this.authService.isLoggedIn()) {
      this.walletService.hasSufficientBalance(total).then(hasBalance => {
        if (!hasBalance) {
          this.notification.error('Insufficient balance to complete the transaction.', 'Error');
          return;
        }

        this.userService.isProfileComplete().subscribe({
          next: (isComplete) => {
            if (!isComplete) {
              this.notification.error('Please complete your profile before proceeding to payment.', 'Shipping Information incomplete');
              this.router.navigate(['/register']);
              return;
            }

            this.walletService.handleSignAndSendTransaction(total).subscribe({
              next: () => {
                this.notification.success('Order successful! You will receive your NFT shortly.', 'Success');
                this.shoppingCartService.clearCart().subscribe({
                  next: () => this.loadCart(),
                  error: (error) => {
                    console.error('Error while clearing the cart:', error);
                    this.notification.error('Error while clearing the cart');
                  }
                });
              },
              error: (error) => {
                console.error('Error during payment:', error);
                this.notification.error('Payment failed', 'Error');
              }
            });
          },
          error: (error) => {
            console.error('Error checking profile', error);
            this.notification.error('Could not check profile', 'Error');
          }
        });
      }).catch(error => {
        console.error('Error checking balance', error);
        this.notification.error('Error checking balance.', 'Error');
      });
    }
  }
}
