import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Globals } from '../global/globals';
import { Router } from '@angular/router';
import { UserDetailDto } from '../dtos/user';
import { UserListDto } from '../dtos/user';
import { OrderCreateDto, OrderDetailDto, OrderListDto } from "../dtos/order";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userBaseUri: string = this.globals.backendUri + '/user';
  private orderBaseUri: string = this.globals.backendUri + '/user/orders';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals,
  ) {
  }

  /**
   * Deletes current logged in user
   *
   */
  deleteUser(): Observable<void> {
    return this.httpClient.delete<void>(this.userBaseUri);
  }

  /**
   * Function which returns all the details about the currently logged-in user
   *
   * @returns UserDetailDto object
   */
  getUserByPublicKey(): Observable<UserDetailDto> {
    return this.httpClient.get<UserDetailDto>(this.userBaseUri);
  }

  /**
   * Updates the user details
   *
   * @param userDetailDto the updated user details
   * @returns updated UserDetailDto object
   */
  updateUser(userDetailDto: UserDetailDto): Observable<UserDetailDto> {
    return this.httpClient.put<UserDetailDto>(this.userBaseUri, userDetailDto);
  }

  /**
   * Checks if the user's profile is complete
   *
   * @returns Observable<boolean>
   */
  isProfileComplete(): Observable<boolean> {
    return this.getUserByPublicKey().pipe(
      map(user => !!(user.firstname && user.lastname && (user.emailAddress || user.phoneNumber) && user.country && user.postalCode && user.addressLine1))
    );
  }

  /**
   * Function which returns all the details about the currently logged-in user
   *
   * @returns UserDetailDto object
   */
  getAllOrders(): Observable<OrderListDto[]> {
    return this.httpClient.get<OrderListDto[]>(this.orderBaseUri)
  }

  /**
   * Function which creates a new order
   *
   * @param orderCreateDto the order to create
   * @returns OrderDetailDto object
   */
  verifyAndCreateOrder(orderCreateDto: OrderCreateDto): Observable<OrderDetailDto> {
    return this.httpClient.post<OrderDetailDto>(this.orderBaseUri, orderCreateDto)
  }
}
