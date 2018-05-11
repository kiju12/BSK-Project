import { Component, OnInit, ViewChild, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { User } from '../../models';
import { AuthenticationService } from '../../services/authentication.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login-modal',
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.scss']
})
export class LoginModalComponent implements OnInit {

  @Input() registerModal;
  @ViewChild('loginModal') public loginModal;
  loginForm: FormGroup;
  user: User;
  @Output() emitStatusAfterLogin = new EventEmitter<any>();

  constructor(private formBuilder: FormBuilder, private authService: AuthenticationService, private toastService: ToastrService) {
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
    this.initUser();
    this.loginForm = this.formBuilder.group({
      userName: [this.user.username, Validators.required],
      password: [this.user.password, Validators.required]
    });
  }

  initUser() {
    this.user = { username: null, password: null };
  }

  loginUser() {
    this.authService.login(this.user).subscribe(() => {
      this.loginModal.hide();
      this.showSuccessAlert();
    },
    error => {
      this.showErrorAlert(error.error);
    },
  () => this.emitLoginStatus());
  }

  emitLoginStatus() {
    this.emitStatusAfterLogin.emit();
  }

  showSuccessAlert() {
    this.toastService.success('Pomyślnie zalogowano!', 'SUKCES!');
  }

  showErrorAlert(error: string) {
    this.toastService.error(error, 'BŁĄD!');
  }
}
