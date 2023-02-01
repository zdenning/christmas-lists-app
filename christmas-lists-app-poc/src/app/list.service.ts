import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Item } from './item';
import { Globals } from './globals';
import { UserService } from './user.service';
import { ItemDetailsComponent } from './item-details/item-details.component';

@Injectable({
  providedIn: 'root'
})
export class ListService {

  private host = 'http://christmaslistapi-prod-2.us-east-1.elasticbeanstalk.com/v1';
  private listGet = '/list';
  private list = '/list';
  private listBuyPost = '/list/buy';
  private listAddPost = '/list';

  constructor(
    private http: HttpClient,
    private userService: UserService) { }

   getList(username: string): Observable<Item[]> {
    return  this.http.get<Item[]>(this.host + this.listGet, {
      params: {
        username: username,
        currentUser: this.userService.getCurrentUser()
      }
    });
  }

  addItem(username: string, name: string, description: string) {
    var item: Item = {
      name: name,
      description: description
    }
    return this.http.post<Item>(this.host + this.listAddPost, item, {
      params: {
        username: username
      }
    })
  }

  buyItem(username: string, itemName: string) {
    var item: Item = {
      name : itemName
    }
    return this.http.post<Item>(this.host + this.listBuyPost, item, {
      params: {
        username: username,
        currentUser: this.userService.getCurrentUser()
      }
    });
  }

  updateItem(username: string, item: Item)
  {
    return this.http.patch<Item>(this.host + this.list, item, {
      params: {
        username: username,
        currentUser: this.userService.getCurrentUser()
      }
    });
  }

  deleteItem(username: string, itemName: string) {
    console.error("****  " + username + " / " + itemName + " / ");
    console.log("****  " + username + " / " + itemName + " / " + this.userService.getCurrentUser());
    return this.http.delete(this.host + this.list, {
      params: {
        username: username,
        currentUser: this.userService.getCurrentUser(),
        gift: itemName
      }
    })
  }
}
