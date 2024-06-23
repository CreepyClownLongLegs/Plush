import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {AdminService} from 'src/app/services/admin.service';
import {NavigationEnd, Router} from '@angular/router';
import {SearchService} from 'src/app/services/search.service';
import {ButtonType} from "../login/login.component";
import {filter, Subscription} from "rxjs";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  isDropdownOpen = false;
  searchTerm: string = '';
  private searchTermSubscription: Subscription;

  constructor(
    public authService: AuthService,
    private searchService: SearchService,
    private router: Router
  ) {

  }

  ngOnInit() {
    this.searchTermSubscription = this.searchService.searchTerm$.subscribe(searchTerm => {
      this.searchTerm = searchTerm;
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
    }
  }

  navigateToCart() {
    this.router.navigate(['/cart']);
  }

  onSearch() {
    this.searchService.setSearchTerm(this.searchTerm);
    this.navigateToHome()
  }

  navigateToHome() {
    this.router.navigate(['/']);
  }

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  protected readonly ButtonType = ButtonType;
}
