import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';
import { environment } from '../../environments/environment';
import { Globals } from '../globals';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';


@Component({
  selector: 'app-assign-role',
  templateUrl: './assign-role.component.html',
  styleUrls: ['./assign-role.component.scss']
})
export class AssignRoleComponent implements OnInit {
  employeeName = '';
  displayPrgAdminName = false;
  displayAdminName = false;
  items: any;
  display;
  role_bounty_id;
  admin_role_bounty_id;
  dbroles1;
  lst1;
  lst2;


  constructor(private http: HttpClient,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,
    private globals:Globals,
    private router: Router) { }

  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)
    
    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }

    this.tssvcommonvalidatorService.isAccessible();

    this.getallEmployee()

  }

  AddBugBountyRole() {
    if (this.valid_employee_id(this.role_bounty_id)) {
      this.http.get(environment.apiURL + 'bugBountyRole/' + this.role_bounty_id, { responseType: 'text' }).subscribe(response => {
        this.role_bounty_id = '';
        this.messageService.add({ severity: 'success', detail: response });
        this.items = response;
        this.getallEmployee()

      }, error => {
        this.messageService.add({ severity: 'error', detail: error.error });
        this.role_bounty_id = '';
      });
    }
  }

  AddBughuntrAdminRole() {
    if (this.valid_employee_id(this.admin_role_bounty_id)) {
      this.http.get(environment.apiURL + 'bugBountyAdminRole/' + this.admin_role_bounty_id, { responseType: 'text' }).subscribe(response => {
        if(response===null){
          this.messageService.add({ severity: 'error', detail: "This Employee ID not exist in ETL Table" });
         }
        this.admin_role_bounty_id = '';
        this.messageService.add({ severity: 'success', detail: response });
        this.items = response;
        this.getallEmployee()
      }, error => {
        this.messageService.add({ severity: 'error', detail: error.error });
        this.admin_role_bounty_id = '';
        
      });
    }
  }

  valid_employee_id(id) {
    var flaggeneralvalidate = false;
    if(id == null || id.length == 0){
      this.messageService.add({ severity: 'error', detail: 'Please enter employee ID' });
    }
   else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", id)) {
      this.messageService.add({ severity: 'error', detail: 'Please enter Valid employee ID' });
    } 
    else {
      flaggeneralvalidate = true;
    }
    return flaggeneralvalidate;
  }

  getprgAdminEmployeeName() {
    if (this.valid_employee_id(this.role_bounty_id) == true) {
      this.http.get(environment.apiURL + 'employee/' + this.role_bounty_id).subscribe(response => {
        if(response[0]===undefined){
          this.messageService.add({ severity: 'error', detail: "Employee ID does't exist" });
         }
        this.employeeName = response[0].full_name;
        this.displayPrgAdminName = true;

      }, error => {
        this.messageService.add({ severity: 'error', detail: "Please enter valid Employee ID"});
      });
    }

  }

  getAdminEmployeeName() {

    if (this.valid_employee_id(this.admin_role_bounty_id) == true) {
      this.http.get(environment.apiURL + 'employee/' + this.admin_role_bounty_id).subscribe(response => {
       if(response===null){
        this.messageService.add({ severity: 'error', detail: "This Employee ID not exist in ETL Table" });
       }
        this.employeeName = response[0].full_name;
        this.displayAdminName = true;
      }, error => {
        this.messageService.add({ severity: 'error', detail: "Please enter valid Employee ID"});
      });
    }


  }

  getallEmployee() {
    var list1 = [];
    var list2 = [];

    this.http.get<any>(environment.apiURL + 'employee').subscribe(response => {

      this.dbroles1 = response;

      for (var i = 0; i < this.dbroles1.length; i++) {
        if (this.dbroles1[i].roleid == 1) {
          list1.push(this.dbroles1[i])

        }
        else if (this.dbroles1[i].roleid == 2) {
          list2.push(this.dbroles1[i])
        }
        else {

        }
      }
      this.lst1 = list1
      this.lst2 = list2
    });




  }

  deleteEmployee(srno) {



    this.confirmationService.confirm({

      message: `Are you sure you want to delete ?`,

      accept: () => {


        this.http.get(environment.apiURL + 'deleterole/' + srno, { responseType: 'text' })

          .subscribe(

            data => {

              this.messageService.add({ severity: 'success', summary: 'Success', detail: "Deleted Succcessfully!" });



              this.getallEmployee();



            },

            error => {

              if (error.error.ERROR_MSG == null) {

                this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error});

                return;

              }

            });

      },
      reject: () => {

        this.messageService.add({ severity: 'warn', summary: 'Warn', detail: 'You have cancelled Delete Request' });

      }

    });

  }

}
