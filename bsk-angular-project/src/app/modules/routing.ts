import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {
  MainPageContentComponent,
  LoggedUsersComponent,
  AdminContentComponent,
  ActivateAccountComponent
} from './';

const appRoutes: Routes = [
  { path: 'home', component: MainPageContentComponent },
  { path: 'zalogowani', component: LoggedUsersComponent },
  { path: 'admin', component: AdminContentComponent },
  {
    path: '',
    children: [
      {
        path: ':codeId/:activationCode',
        component: ActivateAccountComponent
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: '/home',
      }

    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
