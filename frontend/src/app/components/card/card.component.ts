import { Component, Input, OnInit, ElementRef, Renderer2 } from '@angular/core';
import { ShoppingCartService } from '../../services/shopping-cart.service';
import { ToastrService } from 'ngx-toastr';
import { PlushToyListDto } from '../../dtos/plushtoy';
import { ButtonType } from '../login/login.component';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent implements OnInit {

  @Input() plushie: PlushToyListDto;
  @Input() imageUrl!: string;
  rarityPoints: number = 0;

  constructor(
    public authService: AuthService,
    private shoppingCartService: ShoppingCartService,
    private notification: ToastrService,
    private renderer: Renderer2,
    private el: ElementRef
  ) { }

  ngOnInit(): void {
    this.calculateRarity();
    this.setIconBackground();
  }

  protected readonly ButtonType = ButtonType;

  addToCart(id: number) {
    this.shoppingCartService.addToCart(id).subscribe({
      next: () => {
        console.log('Item added to cart successfully');
        this.notification.success('Item added to cart ', 'Success');
      },
      error: error => {
        console.error('Error adding item to cart', error);
        this.notification.error('Error occurred while adding item to cart');
      }
    });
  }

  handleAddToCart(event: Event, plushieId: number) {
    event.preventDefault();
    event.stopPropagation();
    this.addToCart(plushieId);
  }

  getStars(count: number): any[] {
    return new Array(count);
  }

  calculateRarity() {
    this.rarityPoints += this.plushie.strength;
    if (this.plushie.hp <= 50) {
      this.rarityPoints += 1;
    } else if (50 < this.plushie.hp && this.plushie.hp <= 100) {
      this.rarityPoints += 2;
    } else if (100 < this.plushie.hp && this.plushie.hp <= 150) {
      this.rarityPoints += 3;
    } else if (150 < this.plushie.hp && this.plushie.hp <= 200) {
      this.rarityPoints += 4;
    } else {
      this.rarityPoints += 5;
    }
  }

  setIconBackground() {
    let iconBackgroundClass = this.getIconBackground();
    this.renderer.addClass(this.el.nativeElement.querySelector('.icon-image'), iconBackgroundClass);
  }

  getIconBackground(): string {
    if (this.rarityPoints <= 5) {
      return 'icon-image-low';
    } else if (this.rarityPoints <= 8) {
      return 'icon-image-medium';
    } else {
      return 'icon-image-high';
    }
  }
}

