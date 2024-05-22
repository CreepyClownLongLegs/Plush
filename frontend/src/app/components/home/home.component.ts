import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { PlushToyListDto, PlushToySearchDto } from 'src/app/dtos/plushtoy';
import { AdminService } from 'src/app/services/admin.service';
import { SearchService } from 'src/app/services/search.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  plushies: PlushToyListDto[];
  searchParams: PlushToySearchDto = {
    name: ''
  };
  searchTerm: string = '';
  typewriterText: string = "That's all for now, we are working on new stuff tirelessly though, in the meantime play with our pet!";
  private typeWriterInterval: any; // Store interval ID

  constructor(
    public authService: AuthService,
    private adminService: AdminService,
    private searchService: SearchService
  ) { }

  ngOnInit(): void {
    this.searchService.searchTerm$.subscribe(searchTerm => {
      this.searchTerm = searchTerm;
      this.plushiesReload();
    });
  }

  ngOnDestroy(): void {
    clearInterval(this.typeWriterInterval); // Clear the interval when component is destroyed
  }

  plushiesReload() {
    this.searchParams.name = this.searchTerm
    this.adminService.search(this.searchParams)
      .subscribe({
        next: data => {
          this.plushies = data;
        },
        error: error => {
          console.error('Error fetching plushies', error);
          // Display error message to user as notification
        }
      });
  }

  getAllPlushies() {
    this.adminService.getAllPlushToys().subscribe(
      {
        next: data => {
          this.plushies = data;
        },
        error: error => {
          console.error('Error fetching plushies', error);
          //display error message to user as notification
        }
      }
    )
  }

  typeWriter() {
    let i = 0;
    const speed = 100; // Typing speed in milliseconds
    const repeatInterval = 5000;
    this.typeWriterInterval = setInterval(() => {
      if (i < this.typewriterText.length) {
        document.getElementById("typewriter").textContent += this.typewriterText.charAt(i);
        i++;
      } else {
        clearInterval(this.typeWriterInterval); // Clear current interval
        setTimeout(() => {
          document.getElementById("typewriter").textContent = ""; // Clear existing text
          this.typeWriter(); // Start typing again
        }, repeatInterval);
      }
    }, speed);
  }
  // temp function to check user login status
  checkUserStatus() {
    const isLoggedIn = this.authService.isLoggedIn();
    const isConnected = window.solana.isConnected;
    console.log('Is user logged in?', isLoggedIn, '; Is wallet connected?', isConnected);
  }
}

