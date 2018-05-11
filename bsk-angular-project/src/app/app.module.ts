import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MDBBootstrapModule } from 'angular-bootstrap-md';

import { AppComponent } from './app.component';
import {
  MainNavbarComponent,
  MainFooterComponent,
  MainPageContentComponent,
  LoggedUsersComponent,
  AdminContentComponent,
  LoginModalComponent,
  RegisterModalComponent,
  ActivateAccountComponent
} from './modules';
import { AppRoutingModule } from './modules/routing';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthenticationService } from './services/authentication.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { ActivateAccountService } from './services/activate-account.service';

@NgModule({
  declarations: [
    AppComponent,
    MainNavbarComponent,
    MainFooterComponent,
    MainPageContentComponent,
    LoggedUsersComponent,
    AdminContentComponent,
    LoginModalComponent,
    RegisterModalComponent,
    ActivateAccountComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    MDBBootstrapModule.forRoot(),
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  schemas: [],
  providers: [
    HttpClient,
    AuthenticationService,
    ActivateAccountService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
