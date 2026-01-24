import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-smconfig-error',
  templateUrl: './smconfig-error.component.html',
  styleUrls: ['./smconfig-error.component.css']
})
export class SmconfigErrorComponent implements OnInit {

  error: any;
 
  constructor( private route: ActivatedRoute) { 

    this.route
    .queryParams
    .subscribe(params => {
      this.error = params['errorType'];
     
    });
  }

  ngOnInit() {
  }

}
