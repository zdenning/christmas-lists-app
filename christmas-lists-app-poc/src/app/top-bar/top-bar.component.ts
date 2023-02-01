import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
import { User } from '../user';
import { Globals } from '../globals';

@Component({
  selector: 'app-top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css']
})
export class TopBarComponent implements OnInit {

  constructor(
    public router: Router,
    private userService: UserService) { }

  ngOnInit() {
  }

  myList() {
    this.router.navigate([`/mylist/${Globals.currentUser}`]);
  }

  logout() {
    this.userService.setCurrentUser(null);
    this.router.navigate(['/login']);
  }
  
}