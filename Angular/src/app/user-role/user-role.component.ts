import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';
import { Globals } from '../globals';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';

@Component({
  selector: 'app-user-role',
  templateUrl: './user-role.component.html',
  styleUrls: ['./user-role.component.scss']
})
export class UserRoleComponent implements OnInit {
  totalRoles = [];
  uniqueIDs = [];
  UserRole = [];
  name:any;

  constructor(private http: HttpClient,
    private messageService: MessageService,
    private globals:Globals,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,
    private router:Router) { }

  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)

    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }
    this.tssvcommonvalidatorService.isAccessible();
    this.http.get<any>(environment.apiURL + 'AllUsersRole').subscribe(response => {
      this.totalRoles = response;

      this.uniqueIDs = [...new Set(this.totalRoles.map(item => item.employeeName))];
      for(var i=0;i<this.uniqueIDs.length;i++){
        var admin = false, prg_admin = false, researcher = false;
        var obj = this.totalRoles.filter(item => item.employeeName == this.uniqueIDs[i]);
        
        for(var j=0; j<obj.length; j++){
          if(obj[j].roles == "Administrator"){
            admin = true;
          } else if(obj[j].roles == "Bounty Administrator"){
            prg_admin = true;
          } else if(obj[j].roles == "Researcher"){
            researcher = true;
          }
        }
        this.UserRole.push({employeeName: this.uniqueIDs[i], admin: admin, ProgramAdmin:prg_admin, Researcher:researcher});
      }
      
    },error =>{
      this.messageService.add({ severity: 'error', detail: 'You are not a BugHuntr Admin' });
      this.router.navigate(["/"]);
    });
  }

}
