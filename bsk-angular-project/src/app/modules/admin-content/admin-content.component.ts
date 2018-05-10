import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-admin-content',
  templateUrl: './admin-content.component.html',
  styleUrls: ['./admin-content.component.scss']
})
export class AdminContentComponent implements OnInit {

  addItemForm: FormGroup;

    constructor(private formBuilder: FormBuilder) {
      this.initAddItemForm();
    }

    ngOnInit(): void {
    }

    initAddItemForm() {
      this.addItemForm = this.formBuilder.group({
        itemName: ['', [Validators.required, Validators.minLength(4)]],
        itemPrice: ['', Validators.required],
        itemQuantity: ['', Validators.required]
      });
    }
}
