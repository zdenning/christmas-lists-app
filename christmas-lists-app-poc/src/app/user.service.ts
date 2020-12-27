import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable, of } from 'rxjs';
import { User } from './user';
import { Globals } from './globals';
import { Item } from './item';
import { map, catchError } from 'rxjs/operators';
import { Config } from 'protractor';
import { __await } from 'tslib';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private host = 'http://localhost:8080/v1';
  private userPost = '/user';
  private allUsersGet = '/user/all';
  private listGet = '/list';
  private loginGet = '/user/login';

  constructor(private http: HttpClient) { }

  addUser(user: User): Observable<User> {
      localStorage.setItem("currentUser", JSON.stringify(user));
      return this.http.post<User>(this.host + this.userPost, user);
  }

  async getUsers() {
    return await this.http.get<User[]>(this.host + this.allUsersGet).toPromise();
  }

  getList(usrname: string): Observable<Item[]> {
    return this.http.get<Item[]>(this.host + this.listGet, {
      params: {
        username: usrname
      }
    });
  }

  getCurrentUser(): User {
    if (localStorage.getItem("currentUser") == undefined) {
      return JSON.parse(localStorage.getItem("currentUser"));
    }
    else return null;
  }
 
  setCurrentUser(user: User): void {
    localStorage.setItem("currentUser", JSON.stringify(user));
  }

  login(username: string, password: string): Observable<any> {
    return this.http.get(this.host + this.loginGet, {
      params: {
        username: username,
        password: password
      },
      observe: 'response'
    });
  }

}

  

