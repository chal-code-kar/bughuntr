import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Router, ActivatedRoute, } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import { Globals } from '../globals';

@Component({
  selector: 'app-manage-getalldropdown',
  templateUrl: './manage-getalldropdown.component.html',
  styleUrls: ['./manage-getalldropdown.component.scss']
})
export class ManageGetalldropdownComponent implements OnInit {
  srno: any;

  showdropdown;

  constructor(private http: HttpClient,
    private router: Router,
    private globals:Globals,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,

    private route: ActivatedRoute
  ) { }


  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)
  
    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }

    this.tssvcommonvalidatorService.isAccessible();
    this.GetAllDropdown();
  }

  GetAllDropdown() {
    this.http.get<any>(environment.apiURL + 'GetAllDropdown').subscribe(response => {
      this.showdropdown = response;

    })
  }

  deleteDropdown(id) {


    this.confirmationService.confirm({
      message: `Are you sure you want to delete ?`,
      accept: () => {
        this.http.get(environment.apiURL + 'deleteDropdown/' + id, { responseType: 'text' })
          .subscribe(data => {
              this.messageService.add({ severity: 'success', summary: 'Success', detail: "Deleted Succcessfully!" });
             this.GetAllDropdown();
            },
            error => {
              if (error.error.ERROR_MSG == null) {
                this.messageService.add({ severity: 'error', summary: 'Error', detail: "An error has occurred, please try again or contact the administrator" });
                return;
              }
            });



      },
      reject: () => {

        this.messageService.add({ severity: 'warn', summary: 'Warn', detail: 'You have cancelled Delete Request' });

      }

    });

  }

  editOption(id) {

    this.router.navigate(['manage-edit/' + id]);
  }

}
