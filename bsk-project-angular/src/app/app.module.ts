import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';


import { AppComponent } from './app.component';
import { CorsTestService } from './cors-test/cors-test.service';
import { CorsTestComponent } from './cors-test/cors-test.component';



@NgModule({
  declarations: [
    AppComponent,
    CorsTestComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [CorsTestService,
    HttpClient],
  bootstrap: [AppComponent]
})
export class AppModule { }
