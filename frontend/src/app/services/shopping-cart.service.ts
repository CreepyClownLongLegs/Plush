import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global/globals';
import { Observable } from 'rxjs';
import {PlushToyListDto} from "../dtos/plushtoy";

export interface ShoppingCartItemDto {
  publicKey: string;
  itemId: number;
  amount: number;
}

@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {

  private cartBaseUri: string = this.globals.backendUri + "/cart";

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  addToCart(itemId: number): Observable<void> {
    console.log('Adding item to cart', itemId);
    return this.httpClient.post<void>(`${this.cartBaseUri}/add`, itemId);
  }

  deleteFromCart(itemId: number): Observable<void> {
    console.log('Deleting item from cart', itemId);
    return this.httpClient.delete<void>(`${this.cartBaseUri}/delete?itemId=${itemId}`);
  }


  getFullCart(): Observable<PlushToyListDto[]> {
    console.log('Fetching full cart for user');
    return this.httpClient.get<PlushToyListDto[]>(`${this.cartBaseUri}/full`);
  }

}
