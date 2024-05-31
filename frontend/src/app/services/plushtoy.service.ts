import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";
import {PlushToy} from "../dtos/plushtoy";
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

}
