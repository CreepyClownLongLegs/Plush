import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global/globals';
import { Observable } from 'rxjs';
import { PlushToyCartListDto, PlushToyListDto } from "../dtos/plushtoy";

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

  addToCart(plushToyId: number): Observable<void> {
    console.log('Adding item to cart', plushToyId);
    return this.httpClient.post<void>(`${this.cartBaseUri}`, plushToyId);
  }

  deleteFromCart(plushToyId: number): Observable<void> {
    console.log('Deleting item from cart', plushToyId);
    return this.httpClient.delete<void>(`${this.cartBaseUri}?itemId=${plushToyId}`);
  }

  getFullCart(): Observable<PlushToyCartListDto[]> {
    console.log('Fetching full cart for user');
    return this.httpClient.get<PlushToyCartListDto[]>(`${this.cartBaseUri}`);
  }

  decreaseAmount(plushToyId: number): Observable<void> {
    console.log('Decreasing amount of item in cart', plushToyId);
    return this.httpClient.post<void>(`${this.cartBaseUri}/decrease`, plushToyId); // Send itemId directly
  }

  /**
  * Deletes all items from the shopping cart of the current logged-in user
  *
  *@returns Observable<void>
  */
  clearCart(): Observable<void> {
    console.log('Clearing entire cart');
    return this.httpClient.delete<void>(`${this.cartBaseUri}/clear`);
  }

}
