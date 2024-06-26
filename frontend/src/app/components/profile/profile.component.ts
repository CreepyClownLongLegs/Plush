import {Component, OnInit, HostListener, ViewChild} from '@angular/core';
import {UserDetailDto} from 'src/app/dtos/user';
import {UserService} from 'src/app/services/user.service';
import {AuthService} from 'src/app/services/auth.service';
import {Router} from '@angular/router';
import {WalletService} from 'src/app/services/wallet.service';
import {ConfirmationDialogComponent} from "../util/confirmation-dialog/confirmation-dialog.component";
import {OrderListDto} from "../../dtos/order";
import {NotificationService} from "../../services/notification.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  @ViewChild(ConfirmationDialogComponent) confirmationDialog!: ConfirmationDialogComponent;

  userDetail: UserDetailDto = {} as UserDetailDto;
  orders: OrderListDto[];
  xp: number = 0;
  totalXp: number = 0;
  maxXp: number = 300;
  maxWidth: number = 800;
  level: number = 0;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private walletService: WalletService,
    private notification: NotificationService,
    private toastr: ToastrService
  ) {
  }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
      return;
    } else {
      this.fetchUserDetails();
      this.loadOrders();
      this.updateMaxXp();
      this.calculateXP();
      this.calculateWidth();
    }
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: Event): void {
    this.updateMaxXp();
    this.calculateWidth();
  }

  updateMaxXp(): void {
    const screenWidth = window.innerWidth;
    if (screenWidth > 1000) {
      this.maxWidth = 790;
    } else if (screenWidth < 900) {
      this.maxWidth = 290;
    }
  }

  loadOrders() {
    this.userService.getAllOrders()
      .subscribe({
        next: data => {
          this.orders = data;
        },
        error: error => {
          console.error('Error fetching Orders', error);
          this.notification.error('An error occurred while fetching Orders', 'Error fetching Orders')
        }
      });
  }

  calculateXP(): void {
    let totalPrice = 0;
    if (this.orders && this.orders.length > 0) {
      for (const order of this.orders) {
        totalPrice += order.totalPrice;
      }
    }
    this.totalXp = totalPrice;
    this.calculateLevel();
  }

  calculateWidth(): string {
    this.xp = this.totalXp % this.maxXp;
    const percentage = (this.xp / this.maxXp) * 100;
    const width = (percentage / 100) * this.maxWidth;
    return `${width}px`;
  }

  calculateLevel(): void {
    this.level = Math.floor(this.totalXp / this.maxXp);
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

  openOrderHistoryView(): void {
    this.router.navigate(['/orders']);
  }

  updateUserDetails(): void {
    if (!this.validateUserData()) {
      return;
    }
    if (this.userDetail) {
      this.userService.updateUser(this.userDetail).subscribe(
        (updatedUser: UserDetailDto) => {
          this.userDetail = updatedUser;
          this.notification.success('Update successful', 'Success');
        },
        (error) => {
          console.error('Error updating user', error);
          this.notification.error('Could not update user', 'Error');
        }
      );
    }
  }

  validateUserData(): boolean {
    const errors: string[] = [];
    const nameRegex = /^[A-Za-z]+$/;
    const numberRegex = /^[0-9]+$/;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const countryRegex = /^[A-Za-z ]+$/;
    const addressRegex = /[A-Za-z]/; // At least one letter in address

    if (!this.userDetail?.firstname) {
    } else if ((this.userDetail.firstname.trim() === "" || !nameRegex.test(this.userDetail.firstname) || this.userDetail.firstname.length > 255)) {
      errors.push("First name cannot consist of empty spaces, contain numbers, spaces or be longer than 255 letters");
    }

    if (!this.userDetail?.lastname) {
    } else if ((this.userDetail.lastname.trim() === "" || !nameRegex.test(this.userDetail.lastname) || this.userDetail.lastname.length > 255)) {
      errors.push("Last name cannot consist of empty spaces, contain numbers, spaces or be longer than 255 letters");
    }

    if (!this.userDetail?.emailAddress) {
    } else if ((this.userDetail.emailAddress.trim() === "" || !emailRegex.test(this.userDetail.emailAddress) || this.userDetail.emailAddress.length > 255)) {
      errors.push("Email cannot consist of empty spaces, it has to follow the form 'email@domain.com'");
    }

    if (!this.userDetail?.addressLine1) {
    } else if ((this.userDetail.addressLine1.trim() === "" || !addressRegex.test(this.userDetail.addressLine1) || this.userDetail.addressLine1.length > 255)) {
      errors.push("Delivery address cannot consist solely of numbers, empty spaces, or be longer than 255 letters");
    }

    if (!this.userDetail?.addressLine2) {
    } else if ((this.userDetail.addressLine2.trim() === "" || !addressRegex.test(this.userDetail.addressLine2) || this.userDetail.addressLine2.length > 255)) {
      errors.push("Optional address cannot consist solely of numbers, empty spaces, or be longer than 255 letters");
    }

    if (!this.userDetail?.postalCode) {
    } else if ((this.userDetail.postalCode.trim() === "" || !numberRegex.test(this.userDetail.postalCode) || this.userDetail.postalCode.length > 255)) {
      errors.push("Postal Code cannot consist of empty spaces, have symbols which are not number, or be longer than 255 letters");
    }

    if (!this.userDetail?.phoneNumber) {
    } else if (!numberRegex.test(this.userDetail.phoneNumber.replace(/\s+/g, '')) || this.userDetail.phoneNumber.length > 255) {
      errors.push("Phone number cannot consist of empty spaces, be longer than 255 letters, or contain letters and symbols which are not numbers");
    }

    if (!this.userDetail?.country) {
    } else if (!countryRegex.test(this.userDetail.country) || this.userDetail.country.length > 255 || this.userDetail.country.trim() === "") {
      errors.push("Country cannot consist solely of numbers, empty spaces, or be longer than 255 letters");
    }

    if (errors.length > 0) {
      this.toastr.error(errors.join("<br>"), "Update Error", {enableHtml: true, positionClass: 'toast-bottom-right'});
      return false;
    } else {
      return true;
    }
  }


  selectForDeletion(): void {
    this.confirmationDialog.showModal();
  }

  deleteUser() {
    this.userService.deleteUser().subscribe({
      next: () => {
        this.authService.logoutUser();
        this.walletService.disconnectWallet();
        this.router.navigate(['/']).then(() => {
          this.notification.success("Your profile was successfully deleted", "Deletion successful");
        });
      },
      error: error => {
        console.error('Error deleting User', error);
        this.notification.error("Error deleting User", "Could not delete User");
      }
    });
  }
}

