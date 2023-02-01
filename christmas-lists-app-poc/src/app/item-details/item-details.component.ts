import { Component, OnInit } from '@angular/core';
import { Item } from '../item';
import { UserService } from '../user.service';
import { Router, ActivatedRoute } from '@angular/router';
import { ListService } from '../list.service';
import { User } from '../user';
import { Globals } from '../globals';
import { EditItemDialogComponent } from '../edit-item-dialog/edit-item-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-item-details',
  templateUrl: './item-details.component.html',
  styleUrls: ['./item-details.component.css']
})
export class ItemDetailsComponent implements OnInit {
  item: Item;
  username: string;

  constructor(
    private listService: ListService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog
  ) { }

  ngOnInit(): void {
    if (!Globals.currentUser && !localStorage.getItem("currentUser")) {
      this.router.navigate([`/login`]);
    }
    this.userService.setCurrentUser(localStorage.getItem("currentUser"));
    
    this.username = this.route.snapshot.paramMap.get('username');
    this.item = JSON.parse(localStorage.getItem("item"));
  }

  buy() {
    if (window.confirm('Are you sure you want to buy this? This cannot be undone.')) {
      this.listService.buyItem(this.username, this.item.name)
        .subscribe();
      this.item.bought = true;
      localStorage.setItem("item", JSON.stringify(this.item));
    }
  }

  unbuy() {
    //Currently no implementation in the API. ETA MVP2. 
    
  }
  edit() {
    const dialogRef = this.dialog.open(EditItemDialogComponent, {
      width:'75%',
      data: {name: this.item.name, description: this.item.description}
    });

    dialogRef.afterClosed().subscribe(() => {
      localStorage.setItem("currentUser", Globals.currentUser);
      location.reload();
    })
  }


}
