import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {
  MainPageContentComponent,
  LoggedUsersComponent,
  AdminContentComponent,
  ActivateAccountComponent
} from './';
import { AuthGuardService, AdminAuthGuardService } from '../services';

const appRoutes: Routes = [
  { path: 'home', component: MainPageContentComponent },
  {
    path: 'zalogowani',
    component: LoggedUsersComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'admin',
    component: AdminContentComponent,
    canActivate: [AdminAuthGuardService]
  },
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
