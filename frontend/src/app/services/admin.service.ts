import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Globals } from '../global/globals';
import { PlushToyCreationDto, PlushToyDetailsDto, PlushToyListDto } from '../dtos/plushtoy';

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

  /**
   * Send a post request to the backend to create a new plush toy
   *
   * @param plushToy Data of a plush toy to create
   */
  create(plushToy: PlushToyCreationDto): Observable<PlushToyDetailsDto> {
    return this.httpClient.post<PlushToyDetailsDto>(this.adminBaseUri + "/product", plushToy);
  }
}
