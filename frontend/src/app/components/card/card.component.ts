import {Component, Input, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {PlushToyDetailDto, PlushToyListDto} from "../../dtos/plushtoy";
import {AuthService} from "../../services/auth.service";
import {AdminService} from "../../services/admin.service";
import {SearchService} from "../../services/search.service";
import {ShoppingCartService} from "../../services/shopping-cart.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss'
})
export class CardComponent implements OnInit {

  @Input() plushie: PlushToyListDto;
  @Input() imageUrl!: string;

  constructor(
    private shoppingCartService: ShoppingCartService,
    private notification: ToastrService
  ) {
  }

  ngOnInit(): void {
  }

  addToCart(id: number) {

    this.shoppingCartService.addToCart(id).subscribe({
      next: () => {
        console.log('Item added to cart successfully');
        this.notification.success("Item added to cart ", "Success");
      },
      error: error => {
        console.error('Error adding item to cart', error);
        this.notification.info("Log in to use this button ");
      }
    });
  }

  handleAddToCart(event: Event, plushieId: number) {
    event.preventDefault();
    event.stopPropagation();
    this.addToCart(plushieId);
  }

}
