import { Component, OnInit } from '@angular/core';

import { UserService } from "../user.service";
import { User } from '../user';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  usersList: User[];
  currentUser: User;
  constructor(private userService: UserService) { }

  async ngOnInit() {
    await this.getUsers();
  }

  async getUsers() {
    this.currentUser = this.userService.getCurrentUser();
    this.usersList = await this.userService.getUsers();
    this.usersList = this.usersList.filter(
      user => user.username !== this.currentUser.username);
  }
}
