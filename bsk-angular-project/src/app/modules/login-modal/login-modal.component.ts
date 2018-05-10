import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-login-modal',
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.scss']
})
export class LoginModalComponent implements OnInit {

  @Input() registerModal;
  @ViewChild('loginModal') public loginModal;
  loginForm: FormGroup;
  user: any;

  constructor(private formBuilder: FormBuilder) {
    this.user = { username: null, password: null };
    this.initLoginForm();
  }

  ngOnInit() {
    console.log('init');
  }

  show() {
    this.loginModal.show();
  }

  showRegisterModal() {
    this.loginModal.hide();
    this.registerModal.show();
  }

  initLoginForm() {
    this.loginForm = this.formBuilder.group({
      userName: [this.user.username, Validators.required],
      password: [this.user.password, Validators.required]
    });
  }
}
