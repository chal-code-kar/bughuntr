import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ActivatedRoute, Router } from '@angular/router';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';


import { ConfirmationService, MessageService } from 'primeng/api';
import { Globals } from '../globals';

@Component({
  selector: 'app-edit-history',
  templateUrl: './edit-history.component.html',
  styleUrls: ['./edit-history.component.scss']
})
export class EditHistoryComponent implements OnInit {
srno
  data;
  option1
fontname;
releaseverinfo;
releaseitemname;

  constructor(private messageService: MessageService,
    private http: HttpClient,
    private router: Router,
    private globals:Globals,
    private route: ActivatedRoute,
    private tssvcommonvalidatorService: TssvcommonvalidatorService) { }

  ngOnInit(): void {
    this.tssvcommonvalidatorService.isAccessible();
    
    var Roles=JSON.stringify(this.globals.roles)

    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }

    this.srno = this.route.snapshot.params['srno'];
    this.http.get<any>(environment.apiURL + 'history' ).subscribe(response => {
      this.data= response;
     
      for(var i = 0 ; i<this.data.length ; i++){
        if( this.srno == this.data[i].srno){
          this.option1 = this.data[i]
        }
      }
     
    
  });
} 

edithistory() {
 
  if(this.validateEditHistory()) {
  this.http.post(environment.apiURL +`updateHistory/` + this.srno, this.option1, { responseType: 'text' }).subscribe(response => {
   
    this.messageService.add({severity:'success', detail: "History Updated"});
    this.router.navigate(['/admin-history']);
  },error => {
    this.messageService.add({ severity: 'error', detail: error.error });
  });
}
}
validateEditHistory() {
  var flaggeneralvalidate = false;

if (this.option1.fontname == null || this.option1.fontname.trim() == '') {
  this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter  Fontname' });
} else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace", this.option1.fontname)) {
  this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Fontname 0-9 a-z A-Z <space>' });
} else if (this.option1.fontname.length > 50) {
  this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Fontname with less than 50 characters' });
} 
else if (this.option1.releaseverinfo == null || this.option1.releaseverinfo.trim() == '') {
  this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter  Version Information' });
} else if (!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.option1.releaseverinfo)) {
  this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Version Information a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@)' });
} else if (this.option1.releaseverinfo.length>100) {
  this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Version Information with less than 100 characters' });
} 
else if (this.option1.releaseitemname == null || this.option1.releaseitemname.trim() == '') {
  this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Item Name' });
} else if (!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.option1.releaseitemname)) {
  this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Item Name a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@)' });
} else if (this.option1.releaseitemname.length > 1000) {
  this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Item Name with less than 1000 characters' });
} 
 
else {
 flaggeneralvalidate = true;
}
return flaggeneralvalidate;
}


}

