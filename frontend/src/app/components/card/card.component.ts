import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [],
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss'
})
export class CardComponent {
  @Input() title: string;
  @Input() hp: string;
  @Input() price: string;
  @Input() description: string;
  @Input() imageUrl: string;
}
