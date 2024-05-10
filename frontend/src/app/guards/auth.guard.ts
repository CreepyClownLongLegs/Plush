import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {

  constructor(private authService: AuthService,
    private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (this.authService.isLoggedIn()) {
      const requiredRole = route.data.role;
      if (requiredRole) {
        return this.authService.checkIfUserhasRequiredRole(requiredRole);
      }
      // If no role is required, navigare to home page
      this.router.navigate(['/']);
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
