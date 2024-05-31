import {Component, Input, OnInit} from '@angular/core';
import {ShoppingCartService} from "../../services/shopping-cart.service";
import {ToastrService} from "ngx-toastr";
import {PlushToyListDto} from "../../dtos/plushtoy";
import {ButtonType} from "../login/login.component";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss'
})

export class CardComponent implements OnInit {

  @Input() plushie: PlushToyListDto;
  @Input() imageUrl!: string;

  constructor(
    public authService: AuthService,
    private shoppingCartService: ShoppingCartService,
    private notification: ToastrService
  ) {
  }

  ngOnInit(): void {
  }

  protected readonly ButtonType = ButtonType;

  addToCart(id: number) {

    this.shoppingCartService.addToCart(id).subscribe({
      next: () => {
        console.log('Item added to cart successfully');
        this.notification.success("Item added to cart ", "Success");
      },
      error: error => {
        console.error('Error adding item to cart', error);
        this.notification.error("Error occured while adding item to cart");
      }
    });
  }

  handleAddToCart(event: Event, plushieId: number) {
    event.preventDefault();
    event.stopPropagation();
    this.addToCart(plushieId);
  }

}
