import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Message } from '../dtos/message';
import { Observable } from 'rxjs';
import { Globals } from '../global/globals';
import { PlushToyListDto } from '../dtos/plushtoy';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private adminBaseUri: string = this.globals.backendUri + '/admin';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }


  /**
   * Send a delete request to the backend to delete the plushtoy with the given id
   *
   * @param id of the plushtoy to delete
   */
  delete(id: number): Observable<void> {
    return this.httpClient.delete<void>(this.adminBaseUri + "/product/" + id);
  }

  /**
   * Send a get request to the backend to get all the plush toys
   *
   * @returns all plush toys
   */
  getAllPlushToys(): Observable<PlushToyListDto[]> {
    return this.httpClient.get<PlushToyListDto[]>(this.adminBaseUri + "/products");
  }
}
