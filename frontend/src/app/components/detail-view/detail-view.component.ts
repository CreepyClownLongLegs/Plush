import { Component, OnInit } from '@angular/core';
import { NgForOf, NgIf } from '@angular/common';
import { PlushToyColor, PlushToyDetailDto, PlushToySize } from '../../dtos/plushtoy';
import { ActivatedRoute, Router } from '@angular/router';
import { PlushtoyService } from '../../services/plushtoy.service';

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
    private router: Router,
    private route: ActivatedRoute
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
    // Add to cart logic
  }

  buyNow() {
    // Buy now logic
  }
}
