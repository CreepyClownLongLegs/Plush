import {Injectable} from '@angular/core';
import {AuthRequest} from '../dtos/auth-request';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {jwtDecode} from 'jwt-decode';
import {Globals} from '../global/globals';
import {Connection} from '@solana/web3.js';
import {Observable} from "rxjs";
import {NonceRequest} from "../dtos/nonce-request";
import base58 from "bs58";

declare global {
  interface Window {
    solana?: any;
  }
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authBaseUri: string = this.globals.backendUri + '/authentication';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
  ) {
  }

  /**
   * Fetches a nonce from the backend to sign it with the wallet.
   *
   * @param nonceRequest The pubKey of the wallet
   */
  getNonce(nonceRequest: NonceRequest): Observable<{ nonce: string }> {
    return this.httpClient.post<{ nonce: string }>(this.authBaseUri + '/nonce', nonceRequest)
      .pipe(
        tap(response => response.nonce)
      );
  }

  /**
   * Signs the nonce with the wallet.
   *
   * @param nonce The nonce to sign
   */
  async signNonce(nonce: string): Promise<{ signature: string | null, status: 'success' | 'timeout' | 'error' }> {
    const message = `To verify ownership, please sign this nonce: ${nonce}`;
    let timeoutId;
    let signedAfterTimeout = false;

    return new Promise((resolve, reject) => {
      timeoutId = setTimeout(() => {
        signedAfterTimeout = true;
      }, 60000);

      window.solana.signMessage(new TextEncoder().encode(message), "utf8")
        .then(signedMessage => {
          clearTimeout(timeoutId);
          if (signedAfterTimeout) {
            reject({signature: null, status: 'timeout'});
          } else {
            resolve({signature: base58.encode(signedMessage.signature), status: 'success'});
          }
        })
        .catch(error => {
          clearTimeout(timeoutId);
          reject({signature: null, status: 'error', error});
        });
    });
  }

  /**
   * Authenticates the user with the backend.
   *
   * @param authRequest The pubKey of the wallet
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient.post(this.authBaseUri, authRequest, {responseType: "text"}).pipe(
      tap((authResponse: string) => this.setToken(authResponse))
    );
  }

  /**
   * Checks the login status of the user
   */
  isLoggedIn() {
    return !!this.getToken() && (this.getTokenExpirationDate(this.getToken()).valueOf() > new Date().valueOf());
  }

  /**
   * Logs out the user by removing the token from the local storage
   */
  logoutUser() {
    localStorage.removeItem('authToken');
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwtDecode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  userIsAdmin(): boolean {
    return this.getUserRole() === 'ADMIN';
  }

  checkIfUserhasRequiredRole(requiredRole: string): boolean {
    switch (this.getUserRole()) {
      case 'ADMIN':
        return true;
      case 'USER':
        return requiredRole === 'USER';
      default:
        return false;
    }
  }

  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  private getTokenExpirationDate(token: string): Date {
    const decoded: any = jwtDecode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }
}
