import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";

import { UserService } from '../user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Item } from '../item';
import { ListService } from '../list.service';
import { User } from '../user';
import { AddItemDialogComponent } from '../add-item-dialog/add-item-dialog.component';
import { Globals } from '../globals';
// import { AddItemDialogComponent } from '../add-item-dialog/add-item-dialog.component';

@Component({
  selector: 'app-item-list',
  templateUrl: './item-list.component.html',
  styleUrls: ['./item-list.component.css']
})
export class ItemListComponent implements OnInit {
  firstName: string;
  username: string;
  currentUsername: string;
  list;

  constructor(
    private userService: UserService,
    private listService: ListService,
    private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog
    ) { }

  async ngOnInit() {
    if (!Globals.currentUser && !localStorage.getItem("currentUser")) {
      this.router.navigate([`/login`]);
    }

    this.username = this.route.snapshot.paramMap.get('username');
    this.userService.setCurrentUser(localStorage.getItem("currentUser"));
    this.currentUsername = Globals.currentUser;

    var users: User[] = await this.userService.getUsers();
    this.firstName = users.filter(user =>
      user.username == this.username)[0].firstName;

    this.listService.getList(this.username)
      .subscribe(items => this.list = items);
  }

  onSelect(item: Item): void {
    localStorage.setItem("item", JSON.stringify(item));
    this.router.navigate([`/details/${this.username}`]);
  }

  addItem() {
    var itemName: string;
    var itemdesc: string;
    const dialogRef = this.dialog.open(AddItemDialogComponent, {
      width:'75%',
      data: {name: '', description: ''}
    });

    dialogRef.afterClosed().subscribe(() => {
      localStorage.setItem("currentUser", Globals.currentUser);
      location.reload();
    })
  }

}

// export interface DialogData {
//   name: string;
//   description?: string;
// }

// @Component({
//   selector: 'app-add-item-dialog',
//   templateUrl: '../add-item-dialog/add-item-dialog.component.html'
// })
// export class AddItemDialogComponent {

//   constructor(
//     public dialogRef: MatDialogRef<AddItemDialogComponent>,
//     @Inject(MAT_DIALOG_DATA) public data: DialogData,
//     private listService: ListService) { }

//   ngOnInit() {
//   }
  
//   onSubmit(addItemData) {
//     console.warn("Item added!~");
//   }

//   onCancelClick(): void {
//     this.dialogRef.close();
//   }

// }

