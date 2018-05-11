import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { User, ActivationCode } from '../models';


@Injectable()
export class ActivateAccountService {

  readonly ACTIVATE_ACCOUNT_URL = 'http://localhost:8080/api/activate';

  private httpHeaders = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  activateAccount(activationCode: ActivationCode): Observable<any> {
    return this.http.post(this.ACTIVATE_ACCOUNT_URL, activationCode, this.httpHeaders);
  }
}
