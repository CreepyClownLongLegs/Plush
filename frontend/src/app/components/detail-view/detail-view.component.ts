import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {PlushtoyService} from '../../services/plushtoy.service';
import {ShoppingCartService} from "../../services/shopping-cart.service";
import {ToastrService} from "ngx-toastr";
import {PlushToyColor, PlushToy, PlushToySize} from '../../dtos/plushtoy';
import {WalletService} from "../../services/wallet.service";
import {UserService} from "../../services/user.service";
import {OrderDetailDto} from "../../dtos/order";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-detail-view',
  standalone: true,
  imports: [NgForOf, NgIf],
  templateUrl: './detail-view.component.html',
  styleUrls: ['./detail-view.component.scss']
})
export class DetailViewComponent implements OnInit {
  toy: PlushToy = {
    id: 0,
    name: "",
    price: 0,
    description: "",
    taxClass: 0,
    size: PlushToySize.SMALL,
    weight: 0,
    color: PlushToyColor.RED,
    hp: 0,
    imageUrl: "",
    strength: 0,
    productCategories: [],
    attributesDistributions: []
  };
  error: boolean = false;

  constructor(
    private service: PlushtoyService,
    private walletService: WalletService,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private shoppingCartService: ShoppingCartService,
    private userService: UserService,
    private router: Router,
    private authService: AuthService,
  ) {
  }

  ngOnInit(): void {
    const toyID = this.route.snapshot.params['id'];
    this.service.getById(toyID).subscribe({
      next: plushToy => {
        this.toy = plushToy;
      },
      error: error => {
        console.error('Error retrieving product', error);
        this.error = true;
      }
    });
  }

  addToCart() {
    if (!this.authService.isLoggedIn()) {
      this.notification.info("Log in to use this button");
      return;
    }

    this.shoppingCartService.addToCart(this.toy.id).subscribe({
      next: () => {
        console.log('Item added to cart successfully');
        this.notification.success("Item added to cart", "Success");
      },
      error: error => {
        console.error('Error adding item to cart:', error);
        this.notification.error("Error adding item to cart:", "Error");
      }
    });
  }


  buyNow() {
    this.walletService.hasSufficientBalance(this.toy.price).then(hasBalance => {
      if (!hasBalance) {
        this.notification.error('Insufficient balance to complete the transaction.', 'Error');
        return;
      }

      this.userService.isProfileComplete().subscribe({
        next: (isComplete) => {
          if (!isComplete) {
            this.notification.error('Please complete your profile before proceeding to payment.', 'Profile Incomplete');
            this.router.navigate(['/register']);
            return;
          }

          this.walletService.handleSignAndSendTransaction(this.toy.price).subscribe({
            next: (orderDetail: OrderDetailDto) => {
              this.notification.success('Order successful! You will receive your NFT shortly.', 'Success');
              this.router.navigate(['/payment-confirmation'], {state: {orderDetail}});
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

  goBack() {
    window.history.back();
  }

  getStars(count: number): any[] {
    return new Array(count);
  }

  getAttributeNames(): string[] {
    return this.toy.attributesDistributions.map(attr => attr.attribute.name);
  }

}
