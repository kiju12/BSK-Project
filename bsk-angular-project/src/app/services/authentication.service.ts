import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { User } from '../models';


@Injectable()
export class AuthenticationService {

  readonly REGISTER_URL = 'http://localhost:8080/api/register';
  readonly LOGIN_URL = 'http://localhost:8080/api/auth';
  readonly ACTIVATE_ACCOUNT_URL = 'localhost:8080/api/activate';

  private httpHeaders = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  register(user: User): Observable<any> {
    return this.http.post(this.REGISTER_URL, user, this.httpHeaders);
  }
}
