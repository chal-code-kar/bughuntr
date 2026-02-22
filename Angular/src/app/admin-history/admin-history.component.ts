import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Globals } from '../globals';
import { Router } from '@angular/router';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';

@Component({
  selector: 'app-admin-history',
  templateUrl: './admin-history.component.html',
  styleUrls: ['./admin-history.component.scss']
})
export class AdminHistoryComponent implements OnInit {
  announcements;
  srno;
  option1;



  constructor(private http: HttpClient,
    private messageService: MessageService,
    private router:Router,
    private confirmationService: ConfirmationService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,
    private globals:Globals) { }



  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)
    this.tssvcommonvalidatorService.isAccessible();

    if(!Roles.includes("ROLE_Administrator")){
      this.router.navigate(["403"])
    }else{
      this.showhistory();
    }

  }

  showhistory(){
  this.http.get<any>(environment.apiURL + 'history',{ observe: 'response' }).subscribe(response => {

    this.announcements = response.body;
    });
  }

  deletehistory(srno) {

    this.confirmationService.confirm({
      message: `Are you sure you want to delete ?`,
      accept: () => {
        this.http.delete(environment.apiURL + 'deleteHistory/' + srno, { responseType: 'text' })
          .subscribe(
            data => {
              this.messageService.add({ severity: 'success', summary: 'Success', detail: "Deleted Succcessfully!" });
              this.showhistory();
            },
            error => {
              if (error.error.ERROR_MSG == null || error.error.length == 0 )  {
                this.messageService.add({ severity: 'error', summary: 'Error', detail: "An error has occured please try again or contact administrator"});
                return;
              }else{
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

