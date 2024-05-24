import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {UserService} from '../../services/user.service';
import {jwtDecode} from 'jwt-decode';
import {AuthService} from "../../services/auth.service";
import {Router} from '@angular/router';
import { ToastrService } from "ngx-toastr";
import {WalletService} from "../../services/wallet.service";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  @ViewChild('deleteModal') deleteModal: TemplateRef<any>;

  constructor(
    private modalService: NgbModal,
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private notification: ToastrService,
    private walletService: WalletService,
  ) {}

  ngOnInit(): void {
  }

  deleteUser() {
    this.userService.deleteUser().subscribe({
      next: () => {
        this.authService.logoutUser();
        this.walletService.disconnectWallet();

        // redirect to home page and add success notification
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

  openDeleteModal() {
    this.modalService.open(this.deleteModal);
  }
}
