import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { MessageService, ConfirmationService } from 'primeng/api';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import { Globals } from '../globals';




@Component({
  selector: 'app-add-resources',
  templateUrl: './add-resources.component.html',
  styleUrls: ['./add-resources.component.scss']
})
export class AddResourcesComponent implements OnInit {
  guidelines;

  masterid;

  paneltitle;

  paneldescription;

  entries;
  items: any;
  items1;
  Resources = {
    'guidelines': {}

  }
  Resourcesitem = {
    'masterid': {},
    'paneltitle': {},
    'paneldescription': {},
    'entries': {}
  }



  constructor(private http: HttpClient,

    private router: Router,
    private route: ActivatedRoute,
    private globals:Globals,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService) { }
    
  ngOnInit(): void {
    this.tssvcommonvalidatorService.isAccessible();
    this.addResources()
    var Roles=JSON.stringify(this.globals.roles)

    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }
  }

  addResources() {
    this.http.get<any>(environment.apiURL + 'resources').subscribe(response => {

      this.items = response;
    });

  }

  onSubmit() {
    this.Resources.guidelines = this.guidelines
    if (this.validationforAddCategory()) {

      this.http.post(environment.apiURL + 'addcategory', this.Resources, { responseType: 'text' }).subscribe(response => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: "Added Succcessfully!" });
        this.router.navigate(['/Resources']);
      }, error => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error });

      })
    }
  }

  addResourcesitem() {
    this.Resourcesitem.masterid = this.masterid;

    this.Resourcesitem.paneltitle = this.paneltitle;

    this.Resourcesitem.paneldescription = this.paneldescription,

      this.Resourcesitem.entries = this.entries;
    if (this.validationCategoryItems()) {
      this.http.post(environment.apiURL + 'addcategoryitem', this.Resourcesitem, { responseType: 'text' }).subscribe(response => {
        this.messageService.add({ severity: 'success', detail: response });
        this.items1 = response

        this.router.navigate(['/all-resources']);
      }, error => {
        this.messageService.add({ severity: 'error', detail: error.error });
      });
    }
  }

  validationforAddCategory() {
    var flaggeneralvalidate = false;

    if (this.guidelines == null || this.guidelines == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter guidelines' });
    }
    else if (!this.tssvcommonvalidatorService.isRegexValid("description", this.guidelines)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Standard Guidelines .a-zA-Z0-9_()&,-:/ <space> ' });
    }else if (this.guidelines.length > 250) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The length of Standard Guidelines should be less than 60 characters' });
    }
    else {
      flaggeneralvalidate = true;
    }
    return flaggeneralvalidate;

  }
  validationCategoryItems() {

    var flaggeneralvalidate = false;

    if (this.masterid == null) {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please select category' });
    }
    else if (this.paneltitle == null || this.paneltitle == '') {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter title' });
    }
    else if (!this.tssvcommonvalidatorService.isRegexValid("description", this.paneltitle)) {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Standard Guidelines .a-zA-Z0-9_()&,-:/ <space> ' });

    }
    else if (this.paneltitle.length > 100) {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The length of Panel Title should be less than 100 characters' });
    }
    else if (this.paneldescription == null || this.paneldescription == '') {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter description' });
    }
    else if (!this.tssvcommonvalidatorService.isRegexValid("description", this.paneldescription)) {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Panel Description 0-9a-zA-Z. _ () & - <space>' });

    }
    else if (this.paneldescription.length > 1000) {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The length of Panel Description should be less than 1000 characters' });
    }
    else if (this.entries == null) {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter entries' });
    }
    else {
      flaggeneralvalidate = true;

    }

    return flaggeneralvalidate;

  }


}

