import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {
  PlushToy,
  PlushToyListDto,
  PlushToySearchDto,
  ProductCategoryCreationDto,
  ProductCategoryDto
} from '../dtos/plushtoy';

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
    return this.httpClient.get<PlushToyListDto[]>(this.adminBaseUri + "/allProducts");
  }

  /**
   * Send a post request to the backend to create a new plush toy
   *
   * @param plushToy Data of a plush toy to create
   */
  create(plushToy: PlushToy): Observable<PlushToy> {
    return this.httpClient.post<PlushToy>(this.adminBaseUri + "/product", plushToy);
  }

  /**
   * Send a put request to the backend to edit an existing plush toy
   *
   * @param plushToy Data of a plush toy to edit
   */
  edit(plushToy: PlushToy): Observable<PlushToy> {
    return this.httpClient.put<PlushToy>(`${this.adminBaseUri}/product/${plushToy.id}`, plushToy);
  }

  /**
   * Search for horses that match certain search parameters we transfer
   *
   * @param searchParams by which the list of existing horses will be filtered
   * @returns a list of Observables that match the search criteria
   */
  search(searchParams: PlushToySearchDto): Observable<PlushToyListDto[]> {
    return this.httpClient.post<PlushToyListDto[]>(this.adminBaseUri + "/products", searchParams);
  }


  /**
   * Send a get request to the backend to get all categories
   *
   * @returns all categories
   */
  getCategories(): Observable<ProductCategoryDto[]> {
    return this.httpClient.get<ProductCategoryDto[]>(this.adminBaseUri + "/categories");
  }

  /**
   * Send a post request to the backend to create a new category
   *
   * @param category Data of a category to create
   */
  createCategory(category: ProductCategoryCreationDto): Observable<ProductCategoryDto> {
    return this.httpClient.post<ProductCategoryDto>(this.adminBaseUri + "/categories", category);
  }

}
