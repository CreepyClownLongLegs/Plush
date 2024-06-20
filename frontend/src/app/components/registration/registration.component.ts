import { Component, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { AuthService } from "../../services/auth.service";
import { WalletService } from "../../services/wallet.service";
import { UserService } from "src/app/services/user.service";
import { ToastrService } from "ngx-toastr";
import { UserDetailDto } from "src/app/dtos/user";

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
    private toastr: ToastrService,
    private notification: ToastrService,
  ) {
  }

  ngOnInit() {
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
    if (!this.checkUserData()) {
      return;
    }

    if (this.userDetail) {
      this.userService.updateUser(this.userDetail).subscribe(
        (updatedUser: UserDetailDto) => {
          this.userDetail = updatedUser;
          this.toastr.success('You can finish your payment now if you wish :3', 'Success');
          window.history.back();
        },
        (error) => {
          console.error('Error updating user', error);
          this.toastr.error('Could not update user', 'Error');
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

  checkUserData(): boolean {
    if (!this.userDetail?.addressLine1) {
      this.notification.info("Address is empty", "Address Incomplete");
      return false;
    } else if (this.userDetail.addressLine1.trim() === "") {
      this.notification.info("Address cannot consist of empty spaces", "Address Incomplete");
      return false;
    } else if (!this.userDetail.postalCode) {
      this.notification.info("Postal Code is empty", "Postal Code Incomplete");
      return false;
    } else if (this.userDetail.postalCode.trim() === "") {
      this.notification.info("Postal Code cannot consist of empty spaces", "Postal Code Incomplete");
      return false;
    } else if (!this.userDetail.country) {
      this.notification.info("Country is empty", "Country Incomplete");
      return false;
    } else if (this.userDetail.country.trim() === "") {
      this.notification.info("Country cannot consist of empty spaces", "Country Incomplete");
      return false;
    } else if (!this.userDetail.firstname) {
      this.notification.info("First name is empty", "First Name Incomplete");
      return false;
    } else if (this.userDetail.firstname.trim() === "") {
      this.notification.info("First name cannot consist of empty spaces", "First Name Incomplete");
      return false;
    } else if (!this.userDetail.lastname) {
      this.notification.info("Last name is empty", "Last Name Incomplete");
      return false;
    } else if (this.userDetail.lastname.trim() === "") {
      this.notification.info("Last name cannot consist of empty spaces", "Last Name Incomplete");
      return false;
    } else if (!this.userDetail.emailAddress) {
      this.notification.info("Email address is empty", "Email Address Incomplete");
      return false;
    } else if (this.userDetail.emailAddress.trim() === "") {
      this.notification.info("Email address cannot consist of empty spaces", "Email Address Incomplete");
      return false;
    } else {
      return true;
    }
  }

  vanishError() {
    this.error = false;
  }
}
