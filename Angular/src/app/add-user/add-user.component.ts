import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import * as XLSX from 'xlsx';
import { Globals } from '../globals';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {

  emp_id;
  data = [];
  emp;
  uploadedFiles: any[];
  file;
  uploadedFile: File;
  demo;
  arrayBuffer;
  filelist;
  data1;
  UserList;


  constructor(private http: HttpClient,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,
    private globals: Globals,
    private router: Router) { }

  ngOnInit (): void {

    var Roles=JSON.stringify(this.globals.roles)

    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }
    this.tssvcommonvalidatorService.isAccessible();
    this.getUserDetails();
  }

  getUserDetails () {
    this.http.get(environment.apiURL + 'getUsers').subscribe(response => {
      this.data1 = response;
    });
  }

  valid_employee_id (id) {
    var flaggeneralvalidate = false;
    if (id == null || id.length == 0 || !this.tssvcommonvalidatorService.isRegexValid("numeric", id))
    {
      this.messageService.add({ severity: 'error', detail: 'Please enter a valid employee ID' });
    } else
    {
      flaggeneralvalidate = true;
    }
    return flaggeneralvalidate;
  }

  removeUser (e) {
    this.UserList.splice(e, 1);
  }
  getEmployee (e) {
    var test = false;
    for(const i in this.data1.Data){
      if(this.data1.Data[i].emp_id==this.emp_id){
        this.messageService.add({ severity: 'error', detail: 'Employee Id already exists' });
        this.emp_id="";
        return
      }
      
    }
    if (this.valid_employee_id(e) == true)
    {
      this.http.get(environment.apiURL + 'employee/' + e).subscribe(response => {

        if (response[0])
        {

          this.data.push(response[0])
          this.emp_id = null
          this.UserList = [...new Map(this.data.map(item => [item['employee_number'], item])).values()];
        }
        else
        {
          this.messageService.add({ severity: 'error', detail: 'Employee Not Found' });

        }
      }, error => {
        this.messageService.add({ severity: 'error', detail: 'Please enter a valid employee ID' });
      });
    }
  }
  submitUser () {
    if (this.data.length > 0)
    {
      for (var i = 0; i < this.data.length; i++)
      {
        this.submit(parseInt(this.data[i].employee_number))
      }
      this.messageService.add({ severity: 'success', detail: 'Users Added!' });
      this.data = []
      this.UserList = [];
    } else
    {
      this.messageService.add({ severity: 'error', detail: 'Add Users!' });
    }
  }
  submit (e) {
    this.http.post(environment.apiURL + 'addDetail/' + e, { responseType: 'text' }, { responseType: 'text' }).subscribe(response => {
      this.getUserDetails();
    }, error => {
    });
  }

  onSelect (evt: any) {
    this.uploadedFile = evt[0];
    let fileReader = new FileReader();
    fileReader.readAsArrayBuffer(this.uploadedFile);
    fileReader.onload = (e) => {
      this.arrayBuffer = fileReader.result;
      var data = new Uint8Array(this.arrayBuffer);
      var arr = new Array();
      for (var i = 0; i != data.length; ++i) arr[i] = String.fromCharCode(data[i]);
      var bstr = arr.join("");
      var workbook = XLSX.read(bstr, { type: "binary" });
      var first_sheet_name = workbook.SheetNames[0];
      var worksheet = workbook.Sheets[first_sheet_name];
      var arraylist = XLSX.utils.sheet_to_json(worksheet, { raw: true });
      this.filelist = [];
      this.file = arraylist
      for (var i = 0; i < this.file.length; i++)
      {
        this.getEmployee(this.file[i].Employee_Number);
      }

    }

  }
  download () {
    window.open('assets/template/Employelist.xlsx')
  }

}


