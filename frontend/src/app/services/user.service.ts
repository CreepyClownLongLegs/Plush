import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Globals } from '../global/globals';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userBaseUri: string = this.globals.backendUri + '/user/delete';

  constructor(
    private http: HttpClient,
    private globals: Globals,
  ) { }

  deleteUser(): Observable<void> {
    return this.http.delete<void>(this.userBaseUri);
  }
}
