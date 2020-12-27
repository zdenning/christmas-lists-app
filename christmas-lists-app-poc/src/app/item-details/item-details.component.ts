import { Component, OnInit } from '@angular/core';
import { Item } from '../item';

@Component({
  selector: 'app-item-details',
  templateUrl: './item-details.component.html',
  styleUrls: ['./item-details.component.css']
})
export class ItemDetailsComponent implements OnInit {
  itemName: string;
  description: string;

  constructor() { }

  ngOnInit(): void {
    var item: Item = JSON.parse(localStorage.getItem("item"));
    this.itemName = item.name;
    this.description = item.linkOrNotes;
  }

}
