import { NgModule } from '@angular/core';
import { mapToCanActivate, RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { AdminPlushtoyOverviewComponent } from './components/admin/plushtoy/overview/overview.component';
import {DetailViewComponent} from "./components/detail-view/detail-view.component";
import { AdminPlushtoyCreateComponent as AdminPlushtoyCreateComponent } from './components/admin/plushtoy/create/create.component';
import { AdminCategoryCreateComponent } from './components/admin/categories/create/create.component';
import { AdminCategoryOverviewComponent } from './components/admin/categories/overview/overview.component';
import {CartComponent} from "./components/cart/cart.component";
import {UserProfileComponent} from "./components/user-profile/user-profile.component";



const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'detail/:id', component: DetailViewComponent },
  { path: 'cart', component: CartComponent },
  { path: 'profile', component: UserProfileComponent },
  {
    path: 'admin', canActivate: [AuthGuard], data: { role: 'ADMIN' }, children: [
      { path: '', component: AdminPlushtoyOverviewComponent, data: { role: 'ADMIN' } },
      { path: 'new', component: AdminPlushtoyCreateComponent, data: { role: 'ADMIN' } },
      { path: 'categories', component: AdminCategoryOverviewComponent, data: { role: 'ADMIN' } },
      { path: 'categories/new', component: AdminCategoryCreateComponent, data: { role: 'ADMIN' } }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
