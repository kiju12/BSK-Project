import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Item } from '../../models';
import { ItemsService } from '../../services';

@Component({
  selector: 'app-admin-content',
  templateUrl: './admin-content.component.html',
  styleUrls: ['./admin-content.component.scss']
})
export class AdminContentComponent implements OnInit {

  addItemForm: FormGroup;
  itemForm: Item;

  constructor(private formBuilder: FormBuilder, private itemService: ItemsService) {
    this.initAddItemForm();
  }

  ngOnInit(): void {
  }

  initAddItemForm() {
    this.initItem();
    this.addItemForm = this.formBuilder.group({
      itemName: ['', [Validators.required, Validators.minLength(4)]],
      itemPrice: ['', Validators.required],
      itemQuantity: ['', Validators.required]
    });
  }

  initItem() {
    this.itemForm = {
      name: '',
      price: 0,
      quantity: 0
    };
  }

  saveItem() {
    this.itemService.saveItem(this.itemForm).subscribe(response => {
      console.log(response);
    },
    error => {
      console.log(error);
    });
  }
}
