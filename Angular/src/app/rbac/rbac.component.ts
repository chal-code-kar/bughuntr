import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { Globals } from '../globals';

@Component({
  selector: 'app-rbac',
  templateUrl: './rbac.component.html',
  styleUrls: ['./rbac.component.css']
})

export class RbacComponent implements OnInit {

  employeeName = '';
  displayPrgAdminName= false;
  displayAdminName = false;
  PermissionEdited = [];
  items: any;
  roleDescription;
  roleName;
  TotalAccess;
  totalPermission;
  totalRoles;
  keys;
  display;
  ActualPermission = [];
  role_bounty_id;
  admin_role_bounty_id;
  constructor(private http: HttpClient,
    private messageService: MessageService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,
    private globals:Globals,
    private router:Router) { }

  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)

    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }

    this.tssvcommonvalidatorService.isAccessible();
    this.getAccess();
  }

  getAccess(){
    this.http.get(environment.apiURL + 'AccessList').subscribe(response => {
      this.TotalAccess = response;
      this.http.get<any>(environment.apiURL + 'rolesAndPermission').subscribe(response => {
        this.totalPermission = response.totalPermission;
        this.totalRoles = response.totalRoles;
        this.keys = Object.keys(this.TotalAccess);
      },error =>{
        this.messageService.add({ severity: 'error', detail: 'You are not a BugHuntr Admin' });
        this.router.navigate(["/"]);
      });
    }, error => {
      this.messageService.add({ severity: 'error', detail: 'You are not a BugHuntr Admin' });
      this.router.navigate(["/"]);
    });
  }

  returnArray(value){
    return Object.keys(value);
  }

  editPermission(a,b,c,d){
    var data = a + "_" + b + '_' + c;
    if(this.PermissionEdited.includes(data + '_' + !d)){
      this.PermissionEdited.splice(this.PermissionEdited.indexOf(data + '_' + !d),1);
    } else {
      this.PermissionEdited.push(data + '_' + d);
    }
  }

  AddRole(){
    var role = {
      roleName:this.roleName,
      roleDescription:this.roleDescription
    }
    this.http.post(environment.apiURL + 'roles',role, {responseType: 'text'}).subscribe(response => {
      this.messageService.add({ severity: 'success', detail: response });
      this.items = response
    }, error =>{
      this.messageService.add({ severity: 'error', detail: 'You are not a BugHuntr Admin' });
    });
  }

  savePermission(){
    this.display = true;
    this.ActualPermission = [];
    for(var i=0;i<this.PermissionEdited.length;i++){
      var array = this.PermissionEdited[i].split("_");
      var permission = '';
      var status = false;

      for(var j=3;j<array.length -1;j++){
        permission = permission + '_' + array[j];
      }

      permission = permission.substr(1,permission.length);
      if(array[array.length-1] == 'true'){
        status = true;
      }
      this.ActualPermission.push({roleName:array[0], module:array[1], submodule:array[2], permission:permission, status:status});
    }
  }

  UpdatePermisions(){
    this.http.post(environment.apiURL + 'rolesAndPermission',this.ActualPermission,{responseType:'text'}).subscribe(response => {
      this.messageService.add({ severity: 'success', detail: 'Permission Updated' });
      this.getAccess();
      this.PermissionEdited = [];
    }, error =>{
      this.messageService.add({ severity: 'error', detail: 'You are not a BugHuntr Admin' });
      this.router.navigate(["/"]);
    });
  }

  AddBugBountyRole(){
    if(this.valid_employee_id(this.role_bounty_id)){
      this.http.get(environment.apiURL + 'bugBountyRole/' + this.role_bounty_id, {responseType: 'text'}).subscribe(response => {
        this.role_bounty_id = '';
        this.messageService.add({ severity: 'success', detail: response });
        this.items = response;
      }, error =>{
        this.messageService.add({ severity: 'error', detail: error.error });
        this.router.navigate(["/"]);
      });
    }
  }

  AddBughuntrAdminRole(){
    if(this.valid_employee_id(this.admin_role_bounty_id)){
      this.http.get(environment.apiURL + 'bugBountyAdminRole/' + this.admin_role_bounty_id, {responseType: 'text'}).subscribe(response => {
        this.admin_role_bounty_id = '';
        this.messageService.add({ severity: 'success', detail: response });
        this.items = response;
      }, error =>{
        this.messageService.add({ severity: 'error', detail: error.error });
        this.router.navigate(["/"]);
      });
    }
  }

  valid_employee_id(id){
    var flaggeneralvalidate = false;
    if (id == null || id.length == 0 || !this.tssvcommonvalidatorService.isRegexValid("numeric", id)) {
      this.messageService.add({ severity: 'error', detail: 'Please enter Proper employee ID' });
    }else {
      flaggeneralvalidate = true;
    }
    return flaggeneralvalidate;
   }

   getprgAdminEmployeeName(){
     if(this.valid_employee_id(this.role_bounty_id) == true){
      this.http.get(environment.apiURL + 'employee/' + this.role_bounty_id).subscribe(response => {
        this.employeeName = response[0].full_name;;
        this.displayPrgAdminName = true;
      }, error => {
          this.messageService.add({ severity: 'error', detail: error.error });
      });
    }
   }

   getAdminEmployeeName(){
    if(this.valid_employee_id(this.admin_role_bounty_id) == true){
     this.http.get(environment.apiURL + 'employee/' + this.admin_role_bounty_id).subscribe(response => {
       this.employeeName = response[0].full_name;;
       this.displayAdminName = true;
     }, error => {
         this.messageService.add({ severity: 'error', detail: error.error });
     });
    }
   }
}
