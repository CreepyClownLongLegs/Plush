import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AdminPlushtoyOverviewComponent } from './components/admin/plushtoy/overview/overview.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { httpInterceptorProviders } from './interceptors';
import { CardComponent } from './components/card/card.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ItemComponent } from './components/profile/item/item.component';
import { AdminPlushtoyCreateEditComponent } from "./components/admin/plushtoy/create-edit/create-edit.component";
import { AdminComponent } from './components/admin/admin.component';
import { AdminCreateAdminComponent } from './components/admin/create-admin/create-admin.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { ConfirmationDialogComponent } from './components/util/confirmation-dialog/confirmation-dialog.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    CardComponent,
    AdminPlushtoyOverviewComponent,
    ProfileComponent,
    ItemComponent,
    AdminComponent,
    AdminCreateAdminComponent,
    RegistrationComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    AdminPlushtoyCreateEditComponent,
    //needed for Toastr
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    ConfirmationDialogComponent,
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
