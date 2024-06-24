import { Router } from '@angular/router';
import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {PlushToyListDto, PlushToySearchDto} from 'src/app/dtos/plushtoy';
import {SearchService} from 'src/app/services/search.service';
import {PlushtoyService} from 'src/app/services/plushtoy.service';
import {Subscription} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  plushies: PlushToyListDto[];
  searchParams: PlushToySearchDto = {
    name: '',
    categoryId: 0
  };

  searchTerm: string = '';
  typewriterText: string = "That's all for now, we are working on new stuff tirelessly though, in the meantime play with our pet!";
  private typeWriterInterval: any; // Store interval ID
  private searchTermSubscription: Subscription;

  constructor(
    public authService: AuthService,
    private searchService: SearchService,
    private plushToyService: PlushtoyService,
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
  }

  ngOnDestroy(): void {
    clearInterval(this.typeWriterInterval);
    this.searchService.clearSearchTerm();
    this.searchTermSubscription.unsubscribe();

  }

  plushiesReload() {
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
}

