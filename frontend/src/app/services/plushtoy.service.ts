import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";
import {PlushToy, PlushToyListDto, PlushToySearchDto} from "../dtos/plushtoy";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PlushtoyService {

  private plushToyBaseUri: string = this.globals.backendUri + "/plush"

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  getById(id: number): Observable<PlushToy> {
    console.log('Load plush toy details for ' + id);
    return this.httpClient.get<PlushToy>(this.plushToyBaseUri + '/' + id);
  }

  /**
   * Search for plush toys that match certain search parameters we transfer
   *
   * @param searchParams by which the list of existing plush toys will be filtered
   * @returns a list of Observables that match the search criteria
   */
  search(searchParams: PlushToySearchDto): Observable<PlushToyListDto[]> {
    return this.httpClient.post<PlushToyListDto[]>(this.plushToyBaseUri + "/search", searchParams);
  }

  /**
   * Send a get request to the backend to get all the plush toys
   *
   * @returns all plush toys
   */
  getAllPlushToys(): Observable<PlushToyListDto[]> {
    return this.httpClient.get<PlushToyListDto[]>(this.plushToyBaseUri + "/");
  }

}
