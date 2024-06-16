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
  selectedPaymentMethod: string;
  publicKey = "";
  balance = 0;
  userDetail: UserDetailDto | null = null;

  constructor(
    private service: PlushtoyService,
    private shoppingCartService: ShoppingCartService,
    private notification: ToastrService,
    public authService: AuthService,
    private walletService: WalletService,
    private modalService: NgbModal,
    private userService: UserService,
    private router: Router
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
    if (this.cartItems.length === 0) {
      console.error('Please add some items to Cart');
      this.notification.error('You have no Items in your Cart to purchase :\')', 'Error');
      return;
    }

    if (this.authService.isLoggedIn()) {
      //we are logged in, time to fetch user data and check if the address is set
      if (this.checkIfAddressIsSet()) {
        this.notification.success("You are the proud owner of a new plushie now!", "Congrats!!1!");
        this.deleteAllItems();
      } else {
        this.notification.info("Please fill out the empty fields so we can deliver you your plushie(s) :) !", "Shipping Information incomplete");
        return;
      }

    } else {
      this.notification.error('Please Log in with your wallet', 'Error');
    }
  }

  deleteAllItems() {
    this.shoppingCartService.deleteAllItemsFromCart().subscribe({
      next: () => {
        console.log('All items deleted from cart.');
        this.router.navigate(['./']);
        // Optionally, navigate to another page or refresh the cart view
      },
      error: (err) => {
        console.error('Error deleting items from cart:', err);
      }
    });
  }

  resetWalletConnection() {
    this.walletService.disconnectWallet();
    this.publicKey = "";
    this.balance = 0;
  }

  ngOnInit(): void {
    this.loadCart();
    this.fetchUserDetails();
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

  fetchUserDetails(): void {
    this.userService.getUserByPublicKey().subscribe(
      (data: UserDetailDto) => {
        this.userDetail = data;
      },
      (error) => {
        console.error('Error fetching user details', error);
      }
    );
  }

  checkIfAddressIsSet(): boolean {

    if (!this.userDetail?.addressLine1) {
      this.notification.error("No Address given", "Incomplete Address");
      this.router.navigate(['/register']); // Reroute to the register component
      return false;
    } else if (this.userDetail!.addressLine1.trim() === "") {
      this.notification.error("Address cannot consist of empty spaces", "Incomplete Address");
      this.router.navigate(['/register']); // Reroute to the register component
      return false;
    } else if (!this.userDetail!.postalCode) {
      this.notification.info("Postal Code is empty", "Postal Code Incomplete");
      this.router.navigate(['/register']); // Reroute to the register component
      return false;
    } else if (this.userDetail!.postalCode.trim() === "") {
      this.notification.info("Postal Code cannot consist of empty spaces", "Postal Code Incomplete");
      this.router.navigate(['/register']); // Reroute to the register component
      return false;
    } else if (!this.userDetail!.country === null) {
      this.notification.info("Country is empty", "Country Incomplete");
      this.router.navigate(['/register']); // Reroute to the register component
      return false;
    } else if (this.userDetail!.country.trim() === "") {
      this.notification.info("Country cannot consist of empty spaces", "Country Incomplete");
      this.router.navigate(['/register']); // Reroute to the register component
      return false;
    } else {
      return true;
    }
  }


}
