import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Item } from '../item';
import { ListService } from '../list.service';

@Component({
  selector: 'app-item-list',
  templateUrl: './item-list.component.html',
  styleUrls: ['./item-list.component.css']
})
export class ItemListComponent implements OnInit {
  firstName: string;
  username: string;
  list;

  constructor(
    private userService: UserService,
    private listService: ListService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    this.username = this.route.snapshot.paramMap.get('username');
    this.userService.getUsers().subscribe(
      users => this.firstName = users
        .filter(user => user.username === this.username)
      [0].firstName
    );
    this.listService.getList(this.username)
      .subscribe(items => this.list = items);
  }

  onSelect(item: Item): void {
    localStorage.setItem("item", JSON.stringify(item));
    this.router.navigate(['/details']);
  }

}
