import { Component, OnInit } from '@angular/core';
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { User } from '../user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm;
  usersList: User[];
  currentUser: User;

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

  }

  async onSubmit(loginData) {
    // this.userService.getUsers()
    //   .subscribe(userList => this.usersList = userList
    //     .filter(u => {
    //       u.username == loginData.username;
    //       console.warn(u.username == loginData.username);
    //     })
    //   );
    this.usersList = await this.userService.getUsers();
    for (let i = 0; i < this.usersList.length; i++) {
      if (this.usersList[i].username == loginData.username) {
        this.currentUser = this.usersList[i];
      }
    }
    this.userService.login(loginData.username, loginData.password)
      .subscribe((response: Response) => {
        this.userService.setCurrentUser(this.currentUser);
        this.router.navigate(['/users']);
      }, error => {
        window.alert('Invalid username/password');
      });

      
  }


}
