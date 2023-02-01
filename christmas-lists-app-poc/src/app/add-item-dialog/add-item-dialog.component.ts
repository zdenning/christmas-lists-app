import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Router } from '@angular/router';
import { Globals } from '../globals';
import { Item } from '../item';
import { ListService } from '../list.service';


@Component({
  selector: 'app-add-item-dialog',
  templateUrl: './add-item-dialog.component.html',
  styleUrls: ['./add-item-dialog.component.css']
})
export class AddItemDialogComponent {
    addItemForm;
    constructor(
        private formBuilder: FormBuilder,
            private listService: ListService,
            private router: Router) {
              this.addItemForm = this.formBuilder.group({
                name: '',
                description: ''
              }); 
    }

    ngOnInit(): void {
    }

    onSubmit(itemData: Item) {
        this.listService.addItem(
            Globals.currentUser,
            itemData.name,
            itemData.description)
            .subscribe();
        this.router.navigate([`mylist/${Globals.currentUser}`]);
    }

    onClose()
    {
      this.router.navigate([`mylist/${Globals.currentUser}`]);
    }

}


