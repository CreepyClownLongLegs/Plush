import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {AdminService} from 'src/app/services/admin.service';
import {Router} from '@angular/router';
import {SearchService} from 'src/app/services/search.service';
import {ButtonType} from "../login/login.component";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  isDropdownOpen = false;
  searchTerm: string = '';

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
    this.router.navigate(['/cart']);
  }

  onSearch() {
    this.searchService.setSearchTerm(this.searchTerm);
  }

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  protected readonly ButtonType = ButtonType;
}
