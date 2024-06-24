import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { Globals } from '../global/globals';
import {catchError, Observable, throwError} from 'rxjs';
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

  addToCart(itemId: number): Observable<void> {
    console.log('Adding item to cart', itemId);
    return this.httpClient.post<void>(`${this.cartBaseUri}`, itemId);
  }

  deleteFromCart(itemId: number): Observable<void> {
    console.log('Deleting item from cart', itemId);
    return this.httpClient.delete<void>(`${this.cartBaseUri}?itemId=${itemId}`);
  }

  getFullCart(): Observable<PlushToyCartListDto[]> {
    console.log('Fetching full cart for user');
    return this.httpClient.get<PlushToyCartListDto[]>(`${this.cartBaseUri}`);
  }

  decreaseAmount(itemId: number): Observable<void> {
    console.log('Decreasing amount of item in cart', itemId);
    return this.httpClient.post<void>(`${this.cartBaseUri}/decrease`, itemId); // Send itemId directly
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
