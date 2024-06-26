import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {PlushtoyService} from 'src/app/services/plushtoy.service';
import {ProductCategoryDto} from 'src/app/dtos/plushtoy';
import {Router} from '@angular/router';
import {SearchService} from 'src/app/services/search.service';
import {ButtonType} from "../login/login.component";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  isDropdownOpen = false;
  isNavbarCollapsed = false;
  searchTerm: string = '';
  categories: ProductCategoryDto[] = [];
  private searchTermSubscription: Subscription;

  constructor(
    public authService: AuthService,
    private searchService: SearchService,
    private router: Router,
    private plushtoyService: PlushtoyService,
  ) {
  }

  ngOnInit() {
    this.getCategories();
    this.searchTermSubscription = this.searchService.searchTerm$.subscribe(searchTerm => {
      this.searchTerm = searchTerm;
      this.isNavbarCollapsed = true;

    });
  }

  onSearchTermChange() {
    if (this.searchTerm === '') {
      this.searchService.setSearchTerm(this.searchTerm);
    }
  }

  ngOnDestroy() {
    if (this.searchTermSubscription) {
      this.searchTermSubscription.unsubscribe();
      this.isNavbarCollapsed = true;
    }
  }

  getCategories() {
    this.plushtoyService.getAllCategories().subscribe({
      next: (categories: ProductCategoryDto[]) => {
        this.categories = categories;
      },
      error: (err) => {
        console.error('Failed to fetch categories', err);
      }
    });
  }

  searchByCategory(categoryId: number) {
    this.searchTerm = '';
    this.searchService.setCategoryId(categoryId);
    this.toggleNavbar();
    this.router.navigate(['/']);
    this.isDropdownOpen = false;
    this.isNavbarCollapsed = true;

  }

  navigateToCart() {
    this.toggleNavbar();
    this.router.navigate(['/cart']);
  }

  onSearch() {
    this.searchService.setCategoryId(-1);
    this.searchService.setSearchTerm(this.searchTerm);
    this.isDropdownOpen = false;
    this.isNavbarCollapsed = true;
    this.navigateToHome();
  }

  navigateToHome() {
    this.router.navigate(['/']);
    this.isDropdownOpen = false;
    this.isNavbarCollapsed = true;

  }

  clickOnLogo() {
    this.searchTerm = '';
    this.searchService.setSearchTerm('');
    this.onSearch();
    this.router.navigate(['/']);
    this.isDropdownOpen = false;

    this.isNavbarCollapsed = true;

  }

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
    this.getCategories();
  }

  toggleNavbar() {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  protected readonly ButtonType = ButtonType;
}

