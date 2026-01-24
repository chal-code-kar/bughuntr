import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { MessageService, ConfirmationService } from 'primeng/api';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';

@Component({
  selector: 'app-resources-editcategory',
  templateUrl: './resources-editcategory.component.html',
  styleUrls: ['./resources-editcategory.component.scss']
})
export class ResourcesEditcategoryComponent implements OnInit {
  resources;

  items: any[];
  dataresources;
  constructor(private http: HttpClient,

    private router: Router,
    private route: ActivatedRoute,

    private messageService: MessageService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,

    private confirmationService: ConfirmationService) { this.route.params.subscribe(params => this.id = params.id); }

  changeid;
  master;
  guidelines: any;
  id: any;
  paneltitle: any;
  paneldescription: any;
  entries: any;
  Resourcesitems: any;
  masterid: any;

 
  ngOnInit(): void {
    this.tssvcommonvalidatorService.isAccessible();
    this.http.get(environment.apiURL + `getresources/` + this.id).subscribe(response => {
      this.Resourcesitems = response;
      this.guidelines = this.Resourcesitems[0].guidelines;
      this.masterid = this.Resourcesitems[0].masterid;
      this.id = this.Resourcesitems[0].id;
      this.paneltitle = this.Resourcesitems[0].paneltitle;
      this.paneldescription = this.Resourcesitems[0].paneldescription;
      this.entries = this.Resourcesitems[0].entries;
      this.changeid=this.masterid;

    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error.error });
      this.router.navigate(["/"]);


    });
    this.http.get<any>(environment.apiURL + 'resources').subscribe(response => {
    
      this.items = response;
      for (var i = 0; i < this.items.length; i++) {
      if (this.masterid == this.items[i].srno) {
        this.master = this.items[i].guidelines
        
      }
    }
     
    });

    
  }

  sumbit() {

    this.dataresources = {
      
      masterid: this.changeid,
      id: this.id,
      paneltitle: this.paneltitle,
      paneldescription: this.paneldescription,
      entries: this.entries
    }
    if(this.validateedit()){
    this.http.post(environment.apiURL + `updateresources/` + this.id, this.dataresources, { responseType: 'text' }).subscribe(response => {
      this.messageService.add({ severity: 'success', summary: 'Success', detail: "Edited Succcessfully!" });
      this.router.navigate(['/all-resources']);
    }, error => {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: "An error has occurred, please try again or contact the administrator" });

    });
    }
  }

  validateedit() {
      
    var flaggeneralvalidate = false;

    if (this.changeid== null ) {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please select category' });
    }
    else if (this.paneltitle == null || this.paneltitle == '') {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter title' });
    }
    else if(this.paneltitle.length > 100){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The length of Panel Title should be less than 100 characters'})
    }
    else if (!this.tssvcommonvalidatorService.isRegexValid("description", this.paneltitle)) {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in titile .a-zA-Z0-9_()&,-:/ <space>' });

    }
    else if (this.paneldescription == null || this.paneldescription == '') {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter description' });
    }
    else if (!this.tssvcommonvalidatorService.isRegexValid("description", this.paneldescription)) {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed .a-zA-Z0-9_()&,-:/ <space>' });

    }
    else if(this.paneldescription.length > 1000){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The length of Panel Description should be less than 1000 characters'});
    }
    else if (this.entries == null ) {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter entries' });
    }
   
   
   
    else {

      flaggeneralvalidate = true;

    }

    return flaggeneralvalidate;

  }


}
