import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { AuthenticationService } from './authentication.service';
import { Item } from '../models';
import { Observable } from 'rxjs/Observable';

@Injectable({
  providedIn: 'root'
})
export class ItemsService {

  readonly ITEMS_URL = 'http://localhost:8080/items';

  private httpHeaders = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + this.authService.getToken()
    })
  };

  constructor(private http: HttpClient, private authService: AuthenticationService) { }

  saveItem(item: Item): Observable<any> {
    return this.http.post(this.ITEMS_URL, item, this.httpHeaders);
  }

  getItems(): Observable<any> {
    return this.http.get(this.ITEMS_URL, this.httpHeaders);
  }
}
