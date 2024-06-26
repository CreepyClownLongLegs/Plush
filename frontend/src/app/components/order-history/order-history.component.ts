import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";

import {OrderListDto} from "../../dtos/order";
import {CurrencyPipe, DatePipe, NgForOf, NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {UserService} from "../../services/user.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-order-history',
  standalone: true,
  imports: [
    CurrencyPipe,
    NgForOf,
    NgIf,
    RouterLink,
    DatePipe
  ],
  templateUrl: './order-history.component.html',
  styleUrl: './order-history.component.scss'
})
export class OrderHistoryComponent implements OnInit {
  orders: OrderListDto[];
  currentPage: number = 1;
  itemsPerPage: number = 4;

  constructor(
    public authService: AuthService,
    private userService: UserService,
    private notification: ToastrService,
  ) {
  }

  ngOnInit() {
    if (this.authService.isLoggedIn()) {
      this.loadOrders();
    }
  }

  paginatedOrders(): OrderListDto[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    return this.orders ? this.orders.slice(start, end) : [];
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

  totalPages(): number {
    return Math.ceil(this.orders.length / this.itemsPerPage);
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
