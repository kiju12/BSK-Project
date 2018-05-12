import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-main-navbar',
  templateUrl: './main-navbar.component.html',
  styleUrls: ['./main-navbar.component.scss']
})
export class MainNavbarComponent implements OnInit {

  zalogowaniStatus: boolean;
  adminStatus: boolean;
  loginStatus: boolean;

  constructor(private authService: AuthenticationService) { }

  ngOnInit() {
    this.changeValuesInMenu();
  }

  setMenuStatusFalse() {
    this.zalogowaniStatus = false;
    this.adminStatus = false;
    this.loginStatus = false;
  }

  changeValuesInMenu() {
    if (this.authService.isLoggedUser()) {
      this.zalogowaniStatus = true;
    }
    if (this.authService.isLoggedAdmin()) {
      this.adminStatus = true;
    }
    this.loginStatus = true;
    if (!this.authService.isLoggedIn()) {
      this.setMenuStatusFalse();
    }
  }

  logout() {
    this.authService.logout();
    this.changeValuesInMenu();
    this.authService.reloadPage();
  }
}
