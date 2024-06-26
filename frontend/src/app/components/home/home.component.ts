import {Router} from '@angular/router';
import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {PlushToyListDto, PlushToySearchDto} from 'src/app/dtos/plushtoy';
import {SearchService} from 'src/app/services/search.service';
import {PlushtoyService} from 'src/app/services/plushtoy.service';
import {Subscription} from "rxjs";
import {TypewriterService} from '../../services/typewriter.service';
import {AdminService} from 'src/app/services/admin.service';
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  plushies: PlushToyListDto[];
  notificationDisplayed: boolean = false;
  searchParams: PlushToySearchDto = {
    name: '',
    categoryId: 0
  };

  searchTerm: string = '';
  typewriterText: string = "That's all for now, we are working on new stuff tirelessly though, in the meantime play with our pet!";
  private typeWriterInterval: any;
  private searchTermSubscription: Subscription;

  constructor(
    public authService: AuthService,
    private adminService: AdminService,
    private typewriterService: TypewriterService, // Inject TypewriterService
    private searchService: SearchService,
    private plushToyService: PlushtoyService,
    private notification: NotificationService,
    private router: Router
  ) {
    // Reload plushies when route changes (e.g. from category search)
    router.events.subscribe(() => {
      this.plushiesReload();
    });
  }

  ngOnInit(): void {
    this.searchTermSubscription = this.searchService.searchTerm$.subscribe(searchTerm => {
      this.searchTerm = searchTerm;
      this.plushiesReload();
    });

    // Start typewriter effect on component initialization
    this.startTypewriter();
  }

  ngOnDestroy(): void {
    clearInterval(this.typeWriterInterval);
    this.searchService.clearSearchTerm();
    this.searchTermSubscription.unsubscribe();

  }

  plushiesReload() {
    this.notificationDisplayed = false;
    this.searchParams.name = this.searchTerm
    this.searchParams.categoryId = this.searchService.searchCategoryId;
    this.plushToyService.search(this.searchParams)
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
    this.plushToyService.getAllPlushToys().subscribe(
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

  startTypewriter() {
    const speed = 100; // Typing speed in milliseconds
    const repeatInterval = 10000;
    this.typewriterService.typeWriterEffect("typewriter", this.typewriterText, speed, repeatInterval);
  }

  // temp function to check user login status
  checkUserStatus() {
    const isLoggedIn = this.authService.isLoggedIn();
    const isConnected = window.solana.isConnected;
    console.log('Is user logged in?', isLoggedIn, '; Is wallet connected?', isConnected);
  }
}







