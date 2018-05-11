import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { User } from '../../models';
import { AuthenticationService } from '../../services/authentication.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register-modal',
  templateUrl: './register-modal.component.html',
  styleUrls: ['./register-modal.component.scss']
})
export class RegisterModalComponent implements OnInit {

  @Input() loginModal;
  @ViewChild('registerModal') public registerModal;
  registerForm: FormGroup;
  user: User;

  constructor(private formBuilder: FormBuilder, private authService: AuthenticationService, private toastr: ToastrService) {
    this.initRegisterForm();
  }

  ngOnInit() {
  }

  show() {
    this.registerModal.show();
  }

  hideModal() {
    this.registerModal.hide();
  }

  showLoginModal() {
    this.registerModal.hide();
    this.loginModal.show();
  }

  initRegisterForm() {
    this.initUser();
    this.registerForm = this.formBuilder.group({
      userName: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(16)]],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  initUser() {
    this.user = { username: null, password: null, email: null };
  }

  registerUserAccount() {
    this.authService.register(this.user).subscribe(response => {
      this.initRegisterForm();
      this.hideModal();
      this.showSuccessAlert();
    },
    (error: HttpErrorResponse) => {
      this.showErrorAlert('Sprobuj ponownie!');
      console.log(error.error);
    });
  }

  showSuccessAlert() {
    this.toastr.success('Pomyślnie zarejestrowano. Odbierz maila w celu potwierdzenia!', 'SUKCES!');
  }

  showErrorAlert(error: string) {
    this.toastr.error(error, 'BŁĄD!');
  }
}
