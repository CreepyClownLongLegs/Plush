import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {AdminPlushtoyOverviewComponent} from './components/admin/plushtoy/overview/overview.component';
import {
  AdminPlushtoyCreateEditComponent as AdminPlushtoyCreateEditComponent,
  PlushToyCreateEditMode
} from './components/admin/plushtoy/create-edit/create-edit.component';
import {DetailViewComponent} from "./components/detail-view/detail-view.component";
import {AdminCategoryCreateComponent} from './components/admin/categories/create/create.component';
import {AdminCategoryOverviewComponent} from './components/admin/categories/overview/overview.component';
import { ProfileComponent } from './components/profile/profile.component';
import {CartComponent} from "./components/cart/cart.component";


const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'detail/:id', component: DetailViewComponent },
  { path: 'profile', component: ProfileComponent },
    { path: 'cart', component: CartComponent},
  {
    path: 'admin', canActivate: [AuthGuard], data: {role: 'ADMIN'}, children: [
      {path: '', component: AdminPlushtoyOverviewComponent, data: {role: 'ADMIN'}},
      {
        path: 'new',
        component: AdminPlushtoyCreateEditComponent,
        data: {role: 'ADMIN', mode: PlushToyCreateEditMode.create}
      },
      {
        path: 'product/:id/edit',
        component: AdminPlushtoyCreateEditComponent,
        data: {role: 'ADMIN', mode: PlushToyCreateEditMode.edit}
      },
      {path: 'categories', component: AdminCategoryOverviewComponent, data: {role: 'ADMIN'}},
      {path: 'categories/new', component: AdminCategoryCreateComponent, data: {role: 'ADMIN'}}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
