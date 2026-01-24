import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { MessageService } from 'primeng/api';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import { Globals } from '../globals';

@Component({
  selector: 'app-manage-add-dropdown',
  templateUrl: './manage-add-dropdown.component.html',
  styleUrls: ['./manage-add-dropdown.component.scss']
})
export class ManageAddDropdownComponent implements OnInit {
  lookupGroup='';
  lookupValue='';
  lookupDescription='';
  data:any;
  newcat;
  addcat:boolean=false;
  lookupGroupList;
  items = [{ label: 'Add New Option' }];
  constructor(private http: HttpClient,
    private router: Router,
    private globals:Globals,
    private messageService: MessageService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService) { }

  option = {
    "lookupgroup": '',
    "lookupvalue": '',
    "lookupdescription": '',
    "active":true
  }

  ngOnInit(): void {
    this.tssvcommonvalidatorService.isAccessible();

    var Roles=JSON.stringify(this.globals.roles)

    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }

    this.http.get(environment.apiURL + `GetAllDropdown`).subscribe(data => {
      this.data = data;
      var unique = [];
      for (var i = 0; i < this.data.length; i++) {
        unique[i] = this.data[i].lookupgroup;
      }
      var unique1 = unique.filter(function (elem, index, self) {
        return index === self.indexOf(elem);
      })
      this.lookupGroupList = unique1;

    }, error => {
      if (error.error.ERROR_MSG == null) {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: "An error has occurred, please try again or contact the administrator" });
        return;
      }
      this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error.ERROR_MSG });
    });
  }

  validateOption() {
    if (this.lookupGroup == '' || this.lookupGroup.length == 0) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: "Please select lookup group" });
    } else if (this.lookupValue == ''){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: "Please enter Lookup Value" });
    } else if (this.lookupValue.length < 2) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: "Lookup value should have minimum 2 characters" });
    } else if (this.lookupValue.length > 150) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: "Lookup value should have maximum 150 characters" });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("name", this.lookupValue)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: "Only following characters are allowed in lookup value\n" + "0-9 a-z A-Z . _ ( ) & - <space>" });
    } else if (this.lookupDescription == ''){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: "Please enter Lookup Description" });
    } else if(this.lookupDescription.length < 2) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: "Lookup Description should have minimum 2 characters" });
    } else if (this.lookupDescription != '' && this.lookupDescription.length > 260) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: "Lookup Description should have maximum 260 characters" });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("name", this.lookupDescription)) {                                                   
      this.messageService.add({ severity: 'error', summary: 'Error', detail: "Only following characters are allowed in lookup description\n" + "0-9 a-z A-Z . _ ( ) & - <space>" });
    } else {
      this.addOption();
    }
  }

  AddCat(){
    var tag=this.newcat.replace(/\s+/g,'');
    this.lookupGroupList.push(tag)
    this.addcat=false;
    this.newcat=null;
  }

  addOption() {
    this.option.lookupgroup = this.lookupGroup;
    this.option.lookupvalue = this.lookupValue;
    
    this.option.lookupdescription = this.lookupDescription;

    this.http.post(environment.apiURL + 'AddDropdown', this.option, {responseType:'text'} ).subscribe(response => {
      
     this.messageService.add({ severity: 'success', summary: 'success', detail:  "Option added successfully!" });
 
      this.router.navigate(['/manage-getall']);
      
    
    }, error => {
      this.messageService.add({ severity: 'error', summary: 'error', detail: error.error});

    });
  }

}
