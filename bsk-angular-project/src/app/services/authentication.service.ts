import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { User } from '../models';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import * as jwtDecode from 'jwt-decode';

@Injectable()
export class AuthenticationService {

  readonly REGISTER_URL = 'http://localhost:8080/api/register';
  readonly LOGIN_URL = 'http://localhost:8080/api/auth';

  private httpHeaders = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  register(user: User): Observable<any> {
    return this.http.post(this.REGISTER_URL, user, this.httpHeaders);
  }

  login(user): Observable<any> {
    return this.http.post<any>(this.LOGIN_URL, user, this.httpHeaders)
      .map(tokenResp => {
        if (tokenResp && tokenResp.token) {
          console.log(tokenResp);
          localStorage.setItem('currentUser', JSON.stringify({ username: user.username, token: tokenResp.token }));
        }
      });
  }

  getToken(): string {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    const token = currentUser && currentUser.token;
    return token ? token : '';
  }

  private getDecodedAccessToken(): Observable<any> {
    try {
      return jwtDecode(this.getToken());
    } catch (Error) {
      return null;
    }
  }

  getRolesArray(): any[] {
    let rolesArray: any[];
    if (this.isLoggedIn()) {
      rolesArray = this.getDecodedAccessToken()['roles'];
    }
    return rolesArray ? rolesArray : [];
  }

  logout() {
    localStorage.removeItem('currentUser');
  }

  isLoggedIn(): boolean {
    return this.getDecodedAccessToken() ? true : false;
  }

  isLoggedAdmin(): boolean {
    let status = false;
    this.getRolesArray().forEach(role => {
      if (role.authority === 'ROLE_ADMIN') {
        status = true;
      }
    });
    return status;
  }

  isLoggedUser(): boolean {
    let status = false;
    this.getRolesArray().forEach(role => {
      if (role.authority === 'ROLE_USER') {
        status = true;
      }
    });
    return status;
  }

  reloadPage() {
    window.location.reload();
  }

}
