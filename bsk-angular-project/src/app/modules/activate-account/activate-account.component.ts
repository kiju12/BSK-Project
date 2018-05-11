import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ActivationCode } from '../../models';
import { ActivateAccountService } from '../../services/activate-account.service';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.scss']
})
export class ActivateAccountComponent implements OnInit {

  activationCode: ActivationCode = {id: 0, code: ''};
  status: boolean;

  constructor(private activatedRoute: ActivatedRoute, private activateAccountService: ActivateAccountService) {
    this.getActivationCodeFromRoute();
    this.activateAccount();
  }

  ngOnInit() {
  }

  getActivationCodeFromRoute() {
    this.activatedRoute.params.subscribe(params => {
      this.activationCode.id = params['codeId'];
      this.activationCode.code = params['activationCode'];
    },
    error => {
      this.status = false;
      console.log(error);
    });
  }

  activateAccount() {
    this.activateAccountService.activateAccount(this.activationCode).subscribe(() => {
      this.status = true;
    },
    error => {
      this.status = false;
      console.log(error);
    });
  }

}
