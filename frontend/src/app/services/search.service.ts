import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private searchTermSubject: BehaviorSubject<string> = new BehaviorSubject<string>('');
  public searchTerm$: Observable<string> = this.searchTermSubject.asObservable();

  setSearchTerm(searchTerm: string): void {
    this.searchTermSubject.next(searchTerm);
  }

  clearSearchTerm(): void {
    this.searchTermSubject.next('');
  }
}
