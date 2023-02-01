import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { Globals } from '../globals';
import { User } from '../user';
import { UserService } from '../user.service';

@Component({
  selector: 'app-add-friend',
  templateUrl: './add-friend.component.html',
  styleUrls: ['./add-friend.component.css']
})
export class AddFriendComponent implements OnInit {
  addFriendForm;
  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {
    this.addFriendForm = this.formBuilder.group({
      username: ''
    }); 
   }

  ngOnInit(): void {
  }

  onSubmit(user: User) {
    console.warn(Globals.currentUser + "/" + user.username);
    this.userService.addFriend(Globals.currentUser, user.username)
      .subscribe();

    localStorage.setItem("currentUser", Globals.currentUser);
    this.router.navigate(['/users']);
  }

  onClose()
  {
    localStorage.setItem("currentUser", Globals.currentUser);
    this.router.navigate(['/users']);
  }
}
