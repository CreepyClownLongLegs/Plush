import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {OrderDetailDto} from "../../dtos/order";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-payment-confirmation',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './payment-confirmation.component.html',
  styleUrl: './payment-confirmation.component.scss'
})
export class PaymentConfirmationComponent implements OnInit {
  orderDetail: OrderDetailDto;

  constructor(private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    this.orderDetail = navigation?.extras?.state?.orderDetail as OrderDetailDto;
  }

  ngOnInit(): void {
    if (!this.orderDetail) {
       this.router.navigate(['/']);
    }
  }

  goMainPage() {
    this.router.navigate(['/']);
  }

}
