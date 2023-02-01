import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Router } from '@angular/router';
import { Globals } from '../globals';
import { Item } from '../item';
import { ListService } from '../list.service';


@Component({
  selector: 'edit-item-dialog',
  templateUrl: './edit-item-dialog.component.html',
  styleUrls: ['./edit-item-dialog.component.css']
})
export class EditItemDialogComponent {
    editItemForm;
    constructor(
        private formBuilder: FormBuilder,
            private listService: ListService,
            private router: Router) {
              var item: Item = JSON.parse(localStorage.getItem("item"));
              this.editItemForm = this.formBuilder.group({
                name: item.name,
                description: item.description
              }); { 
            
        }
    }

    ngOnInit(): void {
    }

    onSubmit(itemData: Item) {
        this.listService.updateItem(
            Globals.currentUser,
            itemData)
            .subscribe();
        localStorage.setItem("item", JSON.stringify(itemData));
        this.router.navigate([`/details/${Globals.currentUser}`]);
    }

    deleteItem(itemData: Item) {
      // var itemName = encodeURIComponent(itemData.name);
      if (window.confirm('Are you sure you want to delete this? This cannot be undone.')) {
        this.listService.deleteItem(
          Globals.currentUser,
          itemData.name)
          .subscribe();
        this.router.navigate([`/mylist/${Globals.currentUser}`]);
      }
      
    }

    onClose()
    {
      this.router.navigate([`/details/${Globals.currentUser}`]);
    }

}


