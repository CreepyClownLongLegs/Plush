import { Component, OnInit } from '@angular/core';
import { NgForOf, NgIf } from '@angular/common';
import { PlushToyColor, PlushToyDetailDto, PlushToySize } from '../../dtos/plushtoy';
import { ActivatedRoute, Router } from '@angular/router';
import { PlushtoyService } from '../../services/plushtoy.service';
import {ShoppingCartService} from "../../services/shopping-cart.service";
import {AuthService} from "../../services/auth.service";
import {ToastrService} from "ngx-toastr";
import {jwtDecode} from "jwt-decode";

@Component({
  selector: 'app-detail-view',
  standalone: true,
  imports: [NgForOf, NgIf],
  templateUrl: './detail-view.component.html',
  styleUrls: ['./detail-view.component.scss']
})
export class DetailViewComponent implements OnInit {
  toy: PlushToyDetailDto = {
    id: 0,
    name: "",
    price: 0,
    description: "",
    taxClass: 0,
    weight: 0,
    size: PlushToySize.SMALL,
    color: PlushToyColor.RED,
    hp: 0,
    imageUrl: "",
    strength: 0,
    productCategories: [],
  };
  error: boolean = false;

  constructor(
    private service: PlushtoyService,
    private shoppingCartService: ShoppingCartService,
    private route: ActivatedRoute,
    private notification: ToastrService,
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

    this.shoppingCartService.addToCart(this.toy.id).subscribe({
      next: () => {
        console.log('Item added to cart successfully');
        this.notification.success("Item added to cart ", "Success");
      },
      error: error => {
        console.error('Error adding item to cart', error);
        this.notification.info("Log in to use this button ");
        this.error = false;
      }
    });
  }

  buyNow() {
    // Buy now logic
  }

  goBack() {
    window.history.back();  // Use window.history.back() to navigate back
  }
}

