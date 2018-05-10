import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-register-modal',
  templateUrl: './register-modal.component.html',
  styleUrls: ['./register-modal.component.scss']
})
export class RegisterModalComponent implements OnInit {

  @Input() loginModal;
  @ViewChild('registerModal') public registerModal;
  registerForm: FormGroup;
  user: any;

  constructor(private formBuilder: FormBuilder) {
    this.user = { username: null, password: null, email: null };
    this.initRegisterForm();
  }

  ngOnInit() {
  }

  show() {
    this.registerModal.show();
  }

  showLoginModal() {
    this.registerModal.hide();
    this.loginModal.show();
  }

  initRegisterForm() {
    this.registerForm = this.formBuilder.group({
      userName: [this.user.username, Validators.required],
      password: [this.user.password, Validators.required],
      email: [this.user.email, [Validators.required, Validators.email]]
    });
  }

}
