import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { MessageService } from 'primeng/api';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import { Globals } from '../globals';

declare const fixObjectData: any;


@Component({
  selector: 'app-manage-edit-dropdown',
  templateUrl: './manage-edit-dropdown.component.html',
  styleUrls: ['./manage-edit-dropdown.component.scss']
})
export class ManageEditDropdownComponent implements OnInit {
  id: number;
  temp: number;
  option1: any;
  lookupGroupList;
  data;
  data1;
  constructor(private http: HttpClient,
    private router: Router,
    private globals:Globals,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService) {
      this.route.params.subscribe(params => this.temp = params.id);
    this.id = Number(this.temp);
  }

  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)

    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }

    this.tssvcommonvalidatorService.isAccessible();
    this.http.get<any>(environment.apiURL + 'GetAllDropdown').subscribe(response => {
      this.data = response;
      var unique = [];
      for (var i = 0; i < this.data.length; i++) {
        unique[i] = this.data[i].lookupgroup;
      }
      var unique1 = unique.filter(function (elem, index, self) {
        return index === self.indexOf(elem);
      })
      this.lookupGroupList = unique1;

      for (var i = 0; i < this.data.length; i++) {
        if (this.id == this.data[i].id) {
          this.option1 = this.data[i]
        }
      }
    });

  }

  validateOption() {
    var returnString = this.validate();
    if (returnString == null) {
      this.editOption()
    } else {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: returnString });
    }
  }

  validate() {
    if (this.option1.lookupvalue == null || this.option1.lookupvalue.length < 2) {
      return "Lookup value should have minimum 2 characters";
    } else if (this.option1.lookupvalue.length > 150) {
      return "Lookup value should have maximum 150 characters";
    } else if (!this.tssvcommonvalidatorService.isRegexValid("name", this.option1.lookupvalue)) {
      return "Only following characters are allowed in lookup value\n" + "0-9 a-z A-Z . _ ( ) & - <space>";
    } else if (this.option1.lookupdescription == null || this.option1.lookupdescription.length < 2) {
      return "Lookup Description should have minimum 2 characters";
    } else if (this.option1.lookupdescription != null && this.option1.lookupdescription.length > 260) {
      return "Lookup Description should have maximum 260 characters";
    } else if (!this.tssvcommonvalidatorService.isRegexValid("name", this.option1.lookupdescription)) {
      return "Only following characters are allowed in Lookup Description\n" + "0-9 a-z A-Z . _ ( ) & - <space>";
    }
    return null;
  }

  editOption() {

    this.http.post(environment.apiURL + `updateDropdown/` + this.id, this.option1,  {responseType:'text'} ).subscribe(response => {
      this.messageService.add({ severity: 'success', detail: "Option updated successfully" });
      this.router.navigate(['/manage-getall']);
    }, error => {
      this.messageService.add({ severity: 'error', summary: 'error', detail: error.error});
      
    });
  }


}
