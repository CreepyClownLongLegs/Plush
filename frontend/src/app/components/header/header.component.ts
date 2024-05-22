import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import { AdminService } from 'src/app/services/admin.service';
import { PlushToySearchDto } from 'src/app/dtos/plushtoy';
import { NavigationEnd } from '@angular/router';
import { filter } from 'rxjs';
import { SearchService } from 'src/app/services/search.service';

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
    private searchService: SearchService
  ) { }

  ngOnInit() {

  }

  onSearch() {
    this.searchService.setSearchTerm(this.searchTerm);
  }

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

}
