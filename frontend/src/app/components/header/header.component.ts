import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {AdminService} from 'src/app/services/admin.service';
import {PlushToySearchDto} from 'src/app/dtos/plushtoy';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs';
import {SearchService} from 'src/app/services/search.service';
import {ButtonType} from "../login/login.component";
import {ShoppingCartService} from "../../services/shopping-cart.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  isDropdownOpen = false;
  searchTerm: string = '';
  showSearchBar: boolean = false;

  constructor(
    public authService: AuthService,
    private adminService: AdminService,
    private searchService: SearchService,
    private router: Router
  ) {
  }

  ngOnInit() {

  }

  showBag() {
    if (!this.authService.isLoggedIn()) {

    }

  }

  navigateToCart() {
    this.router.navigate(['/cart']);  // Navigate to the cart page
  }

  onSearch() {
    this.searchService.setSearchTerm(this.searchTerm);
  }

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  protected readonly ButtonType = ButtonType;
}
