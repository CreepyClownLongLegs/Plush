import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Globals } from '../global/globals';
import { Router } from '@angular/router';
import { UserDetailDto } from '../dtos/user';
import { UserListDto } from '../dtos/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userBaseUri: string = this.globals.backendUri + '/user';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals,
  ) { }

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
 * @param publicKey the public key of the user to update
 * @param userDetailDto the updated user details
 * @returns updated UserDetailDto object
 */
  updateUser(userDetailDto: UserDetailDto): Observable<UserDetailDto> {
    return this.httpClient.put<UserDetailDto>(this.userBaseUri, userDetailDto);
  }
}
