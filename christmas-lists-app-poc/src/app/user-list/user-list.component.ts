import { Component, OnInit } from '@angular/core';

import { UserService } from "../user.service";
import { User } from '../user';
import { Globals } from '../globals';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AddFriendComponent } from '../add-friend/add-friend.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  usersList;
  
  constructor(
    private userService: UserService,
    private router: Router,
    public dialog: MatDialog) { }

  async ngOnInit() { 
    if (!localStorage.getItem("currentUser")) {
      this.router.navigate([`/login`]);
    }
    else
    {
      this.userService.setCurrentUser(localStorage.getItem("currentUser"));
    }
    this.getUsers();
  }

  async getUsers() {
    this.usersList = await this.userService.getFriends(Globals.currentUser);

  }

  addFriend()
  {
    const dialogRef = this.dialog.open(AddFriendComponent, {
      width:'75%',
      data: {username: ''}
    });
    dialogRef.afterClosed().subscribe(() => {
      localStorage.setItem("currentUser", Globals.currentUser);
      location.reload();
    })
  }
}
