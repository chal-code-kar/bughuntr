import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../environments/environment';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import { Router } from '@angular/router';
import { Globals } from '../globals';
@Component({
  selector: 'app-etldata',
  templateUrl: './etldata.component.html',
  styleUrls: ['./etldata.component.scss']
})
export class ETLDataComponent implements OnInit {
  data;
  constructor(private http: HttpClient,
    private router: Router,
    private globals:Globals,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,) { }

  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)

    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }

    this.tssvcommonvalidatorService.isAccessible();
    this.Get()
  }
  Get() {
    this.http.get<any>(environment.apiURL + 'etldata').subscribe(response => {

      this.data = response;
    });
  }
}
