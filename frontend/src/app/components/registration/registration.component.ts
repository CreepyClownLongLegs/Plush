import { Component, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { AuthService } from "../../services/auth.service";
import { WalletService } from "../../services/wallet.service";
import { UserService } from "src/app/services/user.service";
import { ToastrService } from "ngx-toastr";
import { UserDetailDto } from "src/app/dtos/user";
import {Router} from "@angular/router";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.scss'
})
export class RegistrationComponent implements OnInit {

  @ViewChild("walletModal") walletModal: TemplateRef<any>;
  error = false;
  errorMessage = "";
  publicKey = "";
  userDetail: UserDetailDto | null = null;

  constructor(
    public authService: AuthService,
    private walletService: WalletService,
    private userService: UserService,
    private notification: ToastrService,
    private router: Router
  ) {
  }

  ngOnInit() {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
      return; // Exit the method if the user is not logged in
    }
    if (this.authService.isLoggedIn()) {
      this.walletService.connectWallet().then(async (publicKey: string) => {
        this.publicKey = publicKey;
        this.fetchUserDetails();
      })
        .catch((error) => {
          console.error("Error connecting to wallet:", error);
        });
    }
  }

  updateUserData(): void {
    if (!this.validateUserData()) {
      return;
    }

    if (this.userDetail) {
      this.userService.updateUser(this.userDetail).subscribe(
        (updatedUser: UserDetailDto) => {
          this.userDetail = updatedUser;
          this.notification.success('You can finish your payment now if you wish :3', 'Success');
          window.history.back();
        },
        (error) => {
          console.error('Error updating user', error);
          this.notification.error('Invalid Input, could not update profile', 'Error');
        }
      );
    }
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

  validateUserData(): boolean {
    const errors: string[] = [];
    const nameRegex = /^[A-Za-z]+$/;
    const numberRegex = /^[0-9]+$/;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const countryRegex = /^[A-Za-z0-9\s]+$/;

    if (!this.userDetail?.firstname) {
      errors.push("First name is empty");
    } else if (this.userDetail?.firstname && (this.userDetail.firstname.trim() === "" || !nameRegex.test(this.userDetail.firstname) || this.userDetail.firstname.length > 255)) {
      errors.push("First name is invalid");
    }

    if (!this.userDetail?.lastname) {
      errors.push("Last name is empty");
    } else if (this.userDetail?.lastname && (this.userDetail.lastname.trim() === "" || !nameRegex.test(this.userDetail.lastname) || this.userDetail.lastname.length > 255)) {
      errors.push("Last name is invalid");
    }

    if (!this.userDetail?.emailAddress) {
      errors.push("Email address is empty");
    } else if (this.userDetail?.emailAddress && (this.userDetail.emailAddress.trim() === "" || !emailRegex.test(this.userDetail.emailAddress) || this.userDetail.emailAddress.length > 255)) {
      errors.push("Email address is invalid");
    }

    if (!this.userDetail?.addressLine1) {
      errors.push("Address is empty");
    } else if (this.userDetail?.addressLine1 && (this.userDetail.addressLine1.trim() === "" || this.userDetail.addressLine1.length > 255)) {
      errors.push("Address is invalid");
    }

    if (!this.userDetail?.postalCode) {
      errors.push("Postal Code is empty");
    } else if (this.userDetail?.postalCode && (this.userDetail.postalCode.trim() === "" || !numberRegex.test(this.userDetail.postalCode) || this.userDetail.postalCode.length > 255)) {
      errors.push("Postal Code is invalid");
    }

    if (!this.userDetail?.country) {
      errors.push("Country is empty");
    } else if (this.userDetail?.country && (this.userDetail.country.trim() === "" || !countryRegex.test(this.userDetail.country) || this.userDetail.country.length > 255)) {
      errors.push("Country is invalid");
    }

    if (errors.length > 0) {
      this.notification.info(errors.join("<br>"), "Registration Error", { enableHtml: true });
      return false;
    } else {
      return true;
    }
  }

  vanishError() {
    this.error = false;
  }
}
