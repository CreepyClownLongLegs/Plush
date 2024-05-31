import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { UserDetailDto } from 'src/app/dtos/user';
import { UserService } from 'src/app/services/user.service';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { WalletService } from 'src/app/services/wallet.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  @ViewChild('deleteModal') deleteModal: TemplateRef<any>;

  userDetail: UserDetailDto | null = null;
  xp: number = 100;    // xp we have now till next level
  totalXp: number = 500; // Total xp
  maxXp: number = 140; // Example value for maximum XP
  maxWidth: number = 540; // Mhow much xp is needed till next level
  level: number = 1;

  constructor(
    private modalService: NgbModal,
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService,
    private walletService: WalletService,
  ) { }

  ngOnInit(): void {
    this.calculateLevel();
    this.fetchUserDetails();
  }

  calculateWidth(): string {
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
        console.log(data);
      },
      (error) => {
        console.error('Error fetching user details', error);
      }
    );
  }

  updateUserDetails(): void {
    if (this.userDetail) {
      this.userService.updateUser(this.userDetail).subscribe(
        (updatedUser: UserDetailDto) => {
          this.userDetail = updatedUser;
          this.toastr.success('Update successful', 'Success');
        },
        (error) => {
          console.error('Error updating user', error);
          this.toastr.error('Could not update user', 'Error');
        }
      );
    }
  }

  deleteUser() {
    this.userService.deleteUser().subscribe({
      next: () => {
        this.authService.logoutUser();
        this.walletService.disconnectWallet();

        // redirect to home page and add success notification
        this.router.navigate(['/']).then(() => {
          this.toastr.success("Your profile was successfully deleted", "Deletion successful");
        });
      },
      error: error => {
        console.error('Error deleting User', error);
        this.toastr.error("Error deleting User", "Could not delete User");
      }
    });
  }

  openDeleteModal() {
    this.modalService.open(this.deleteModal);
  }
}
