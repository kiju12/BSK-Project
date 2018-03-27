import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthenticationResponse, ValidationErrors, SimpleObject } from './test-objects';

import { Observable } from 'rxjs/Observable';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable()
export class CorsTestService {

  restURL = 'http://localhost:8080/';

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${this.restURL}auth`, { username: username, password: password }, httpOptions);
  }

  register(username: string, password: string, enabled: boolean, email: string): Observable<ValidationErrors> {
    return this.http.post<ValidationErrors>(`${this.restURL}register`,
      { username: username, password: password, enabled: enabled, email: email, authorities: [] }, httpOptions);
  }

  anyoneContent(): Observable<SimpleObject> {
    return this.http.get<SimpleObject>(`${this.restURL}content/anyone`, httpOptions);
  }

  userContent(token: string) {
    return this.http.get<SimpleObject>(`${this.restURL}content/user`, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      })
    });
  }

  adminContent(token: string) {
    return this.http.get<SimpleObject>(`${this.restURL}content/admin`, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      })
    });
  }
}
