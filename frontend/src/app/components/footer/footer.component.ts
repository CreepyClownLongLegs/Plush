import { Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { ProductCategoryDto } from 'src/app/dtos/plushtoy';
import { PlushtoyService } from 'src/app/services/plushtoy.service';
import { SearchService } from 'src/app/services/search.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
  categories: ProductCategoryDto[] = [];

  constructor(
    private searchService: SearchService,
    private plushtoyService: PlushtoyService,
    private router: Router) {
      this.categories = this.plushtoyService.categories;
    }

  ngOnInit() {
    //this.getCategories();
  }

  searchByCategory(categoryId: number) {
    this.searchService.setCategoryId(categoryId);
    // Scroll to top
    window.scrollTo(0, 0);
    this.router.navigate(['/']);
  }

  getCategories() {
    this.categories = this.plushtoyService.categories
    this.plushtoyService.getAllCategories().subscribe(categories => {
      this.categories = categories;
    });
  }

}
