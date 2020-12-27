import { Component, OnInit } from '@angular/core';
import { FormBuilder } from "@angular/forms";
import { UserService } from "../user.service";
import { User } from '../user';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {
  createUserForm;
  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private router: Router) {
      this.createUserForm = this.formBuilder.group({
        username: '',
        password: '',
        reEnterPassword: '',
        firstName: ''
      });
    }

  ngOnInit(): void {
  }

  onSubmit(userData) {
      this.userService.addUser(userData as User)
        .subscribe();
      console.log(`Submitted create user:`, userData);
      this.router.navigate(['/users']);
  }

}
