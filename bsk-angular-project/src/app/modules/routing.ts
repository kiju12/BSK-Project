import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {
  MainPageContentComponent,
  LoggedUsersComponent,
  AdminContentComponent
} from './';

const appRoutes: Routes = [
  { path: 'home', component: MainPageContentComponent },
  { path: 'zalogowani', component: LoggedUsersComponent },
  { path: 'admin', component: AdminContentComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
