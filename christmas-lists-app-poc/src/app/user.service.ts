import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable, of } from 'rxjs';
import { User } from './user';
import { Globals } from './globals';
import { Item } from './item';
import { map, catchError } from 'rxjs/operators';
import { Config } from 'protractor';
import { __await } from 'tslib';
import { resolve } from 'dns';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  //private host = 'http://localhost:8080/v1';
  private host = 'http://christmaslistapi-prod-2.us-east-1.elasticbeanstalk.com/v1';
  private userPost = '/user';
  private allUsersGet = '/user/all';
  private friendsGet = '/user/friends';
  private listGet = '/list';
  private loginGet = '/user/login';

  constructor(private http: HttpClient) { }

  addUser(user: User): Observable<User> {
      this.setCurrentUser(user.username);
      return this.http.post<User>(this.host + this.userPost, user);
  }

  async getUsers() {
    return await this.http.get<User[]>(this.host + this.allUsersGet).toPromise();
  }

  async getFriends(username: string) {
    if (null == username) username = localStorage.getItem("currentUser");
    return await this.http.get<User[]>(this.host + this.friendsGet, {
      params: {
        username: username
      }
    }).toPromise();
  }

  addFriend(username: string, friendName: string)
  {
    return this.http.post(this.host + this.friendsGet, null, {
      params: {
        username: username,
        friend: friendName
      }
    });
  }

  getList(usrname: string): Observable<Item[]> {
    return this.http.get<Item[]>(this.host + this.listGet, {
      params: {
        username: usrname
      }
    });
  }

  getCurrentUser(): string {
    return Globals.currentUser;
  }
 
  setCurrentUser(username: string): void {
    Globals.currentUser = username;
    localStorage.setItem("currentUser", Globals.currentUser);
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

  

