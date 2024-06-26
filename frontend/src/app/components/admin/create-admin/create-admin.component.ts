import {Component, OnInit} from '@angular/core';
import {UserListDto} from 'src/app/dtos/user';
import {AdminService} from "../../../services/admin.service";
import {NotificationService} from "../../../services/notification.service";

@Component({
  selector: 'app-admin-create-admin',
  templateUrl: './create-admin.component.html',
  styleUrls: ['./create-admin.component.scss']
})
export class AdminCreateAdminComponent implements OnInit {
  users: UserListDto[] = [];
  searchTerm: string = '';
  currentPage: number = 1;
  itemsPerPage: number = 4;

  constructor(
    private adminService: AdminService,
    private notification: NotificationService) {
  }

  ngOnInit(): void {
    this.fetchUsers();
  }

  fetchUsers(): void {
    this.adminService.getAllUsers().subscribe(
      (users: UserListDto[]) => this.users = users,
      error => this.notification.error('Error fetching users', 'Error')
    );
  }

  updateUserAdminStatus(user: UserListDto): void {
    user.admin = true;
    this.adminService.updateUserAdminStatus(user).subscribe(
      () => this.notification.success('User updated successfully', 'Success'),
      error => this.notification.error('Error updating user', 'Error')
    );
  }

  filterUsers(): UserListDto[] {
    const filteredUsers = this.users.filter(user =>
      user.firstname?.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      user.lastname?.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      user.publicKey.includes(this.searchTerm)
    );
    return filteredUsers.sort((a, b) => a.publicKey.localeCompare(b.publicKey));
  }

  paginatedUsers(): UserListDto[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    return this.filterUsers().slice(start, end);
  }

  totalPages(): number {
    return Math.ceil(this.filterUsers().length / this.itemsPerPage);
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages()) {
      this.currentPage++;
    }
  }

  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }
}
