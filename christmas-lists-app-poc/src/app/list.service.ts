import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Item } from './item';
import { Globals } from './globals';

@Injectable({
  providedIn: 'root'
})
export class ListService {

  private host = 'http://localhost:8080/v1';
  private listGet = '/list';

  constructor(private http: HttpClient) { }

   getList(usrname: string): Observable<Item[]> {
    return  this.http.get<Item[]>(this.host + this.listGet, {
      params: {
        username: usrname,
        currentUser: Globals.currentUser
      }
    });
  }
}
