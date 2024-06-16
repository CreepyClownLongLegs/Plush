import { Component, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { AuthService } from "../../services/auth.service";
import { AuthRequest } from "../../dtos/auth-request";
import { NonceRequest } from "../../dtos/nonce-request";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ToastrService } from "ngx-toastr";
import { WalletService } from "../../services/wallet.service";
import { UserDetailDto } from "src/app/dtos/user";
import { UserService } from "src/app/services/user.service";
import { Router } from "@angular/router";


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
  balance = 0;
  userDetail: UserDetailDto | null = null;

  constructor(
    public authService: AuthService,
    private walletService: WalletService,
    private userService: UserService,
    private modalService: NgbModal,
    private toastr: ToastrService,
    private notification: ToastrService,
    private router: Router
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

  updateAddress(): void {
    if (!this.checkIfAddressIsSet()) {
      return;
    }

    if (this.userDetail) {
      this.userService.updateUser(this.userDetail).subscribe(
        (updatedUser: UserDetailDto) => {
          this.userDetail = updatedUser;
          this.toastr.success('You can finish your payment now if you wish :3', 'Success');
          this.router.navigate(['./cart']);
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

  checkIfAddressIsSet(): boolean {
    if (!this.userDetail?.addressLine1) {
      this.notification.info("Address is empty", "Address Incomplete");
      this.router.navigate(['/register']); // Reroute to the register component
      return false;
    } else if (this.userDetail!.addressLine1.trim() === "") {
      this.notification.info("Address cannot consist of empty spaces", "Address Incomplete");
      return false;
    } else if (!this.userDetail!.postalCode) {
      this.notification.info("Postal Code is empty", "Postal Code Incomplete");
      return false;
    } else if (this.userDetail!.postalCode.trim() === "") {
      this.notification.info("Postal Code cannot consist of empty spaces", "Postal Code Incomplete");
      return false;
    } else if (!this.userDetail!.country) {
      this.notification.info("Country is empty", "Country Incomplete");
      return false;
    } else if (this.userDetail!.country.trim() === "") {
      this.notification.info("Country cannot consist of empty spaces", "Country Incomplete");
      return false;
    } else {
      return true;
    }
  }

  vanishError() {
    this.error = false;
  }
}

