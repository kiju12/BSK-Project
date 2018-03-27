import { Component, OnInit } from '@angular/core';
import { CorsTestService } from './cors-test.service';

@Component({
  selector: 'app-cors-test',
  templateUrl: './cors-test.component.html',
  styleUrls: []
})
export class CorsTestComponent implements OnInit {

  constructor(private service: CorsTestService) { }

  ngOnInit() {
    this.tests();
  }

  private tests() {
    console.log('WIELKIE TESTY JEDNOSTKOWE - AHOJ');
    let adminToken: string = null;

    console.log('1. Content for anyone test:');
    this.service.anyoneContent().subscribe(anyOneContent => {
      console.log(`*completed (${anyOneContent.text})`);

      console.log('2. Authentication tests:');
      this.service.login('admin', 'admin123').subscribe(res => {
        adminToken = res.token;
        if (adminToken != null) {
          console.log('*completed - Admin Token assigned.');
        }

        console.log('3. Content for admin test:');
        this.service.adminContent(adminToken).subscribe(adminContent => {
          console.log(`*completed - (${adminContent.text})`);


          console.log('4. User content for admin test:');
          this.service.userContent(adminToken).subscribe(userContent => {
            console.log(`*completed - (${userContent.text})`);
            console.log('WSZYSTKO GRA');
          },
            err => {
              console.log(`*failed - user content for admin test:`);
            });

        },
          err => {
            console.log(`*failed - content for admin test`);
          });

      },
        err => {
          console.log(`*failed - Authentication tests`);
        });

    },
      err => {
        console.log(`*Content for anyone test failed.`);
      });


  }
}
