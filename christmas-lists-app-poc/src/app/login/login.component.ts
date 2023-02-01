import { Component, OnInit } from '@angular/core';
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { User } from '../user';
import { Globals } from '../globals';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm;
  usersList: User[];

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService) {
    this.loginForm = this.formBuilder.group({
      username: '',
      password: '',
    });
  }

  ngOnInit(): void {
    if (null != Globals.currentUser) {
      this.router.navigate(['/users']);
    }
  }

  async onSubmit(loginData) {
    console.warn(loginData.username + " **** " + loginData.password);
    this.usersList = await this.userService.getUsers();
    for (let i = 0; i < this.usersList.length; i++) {
      if (this.usersList[i].username == loginData.username) {
        this.userService.setCurrentUser(this.usersList[i].username);
      }
    }
    this.userService.login(loginData.username, loginData.password)
      .subscribe((response: Response) => {
        // this.userService.setCurrentUser(Globals.currentUser);
        this.router.navigate(['/users']);
      }, error => {
        window.alert('Invalid username/password');
      });

      
  }


}
