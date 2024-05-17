import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(
    public authService: AuthService,
  ) {
  }

  ngOnInit() {
  }

  // temp function to check user login status
  checkUserStatus() {
    const isLoggedIn = this.authService.isLoggedIn();
    const isConnected = window.solana.isConnected;
    console.log('Is user logged in?', isLoggedIn, '; Is wallet connected?', isConnected);
  }
}
