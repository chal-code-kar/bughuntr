import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';

import { ConfirmationService, MessageService } from 'primeng/api';
import { Globals } from '../globals';

@Component({
  selector: 'app-history-insert',
  templateUrl: './history-insert.component.html',
  styleUrls: ['./history-insert.component.scss']
})
export class HistoryInsertComponent implements OnInit {
  fontname;
  releaseverinfo;
  releaseitemname;
  response;

  constructor(private messageService: MessageService,
    private router: Router,
    private http: HttpClient,
    private globals:Globals,
    private tssvcommonvalidatorService: TssvcommonvalidatorService) { }

  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)
  
    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }
    this.tssvcommonvalidatorService.isAccessible();
  }

  Historyinsert() {
    var data =
    {
      fontname: this.fontname,
      releaseverinfo: this.releaseverinfo,
      releaseitemname: this.releaseitemname
    }
    if(this.validateAddHistory()) {
    this.http.post(environment.apiURL + 'Posthistory', data, { responseType: 'text' }).subscribe(response => {

      this.messageService.add({ severity: 'success', detail: "History Added" });
      this.router.navigate(['/admin-history']);
    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error });
    });

  }
}
  validateAddHistory() {
    var flaggeneralvalidate = false;

   if (this.fontname == null || this.fontname.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter  Fontname' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace", this.fontname)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Fontname 0-9 a-z A-Z <space>' });
    } else if (this.fontname.length > 50) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The length of Fontname should be less than 50 characters' });
    } 
    else if (this.releaseverinfo == null || this.releaseverinfo.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter  Version Information' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.releaseverinfo)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Version Information a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@)' });
    } else if (this.releaseverinfo.length>100) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The length of Version Information should be less than 100 characters' });
    } 
    else if (this.releaseitemname == null || this.releaseitemname.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Item Name' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.releaseitemname)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Item Name a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@)' });
    } else if (this.releaseitemname.length > 1000) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The length of Item Name should be less than 1000 characters' });
    } 
    else {
      return true;
    }
  }
}


