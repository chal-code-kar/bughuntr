import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import { Globals } from '../globals';

@Component({
  selector: 'app-all-resources',
  templateUrl: './all-resources.component.html',
  styleUrls: ['./all-resources.component.scss']
})
export class AllResourcesComponent implements OnInit {
  resources;
  srno;
  constructor(private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private globals:Globals,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,
    private confirmationService: ConfirmationService) { }



  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)

    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }
    this.tssvcommonvalidatorService.isAccessible();
    this.getResources();
  }


  getResources() {
    this.srno = this.route.snapshot.params['srno'];
    this.http.get<any>(environment.apiURL + 'getresources').subscribe(response => {
      this.resources = response;
    })

  }

  deleteResources(id) {

    this.confirmationService.confirm({

      message: `Are you sure you want to delete ?`,

      accept: () => {

        this.http.delete(environment.apiURL + 'deleteResources/' + id, { responseType: 'text' })
          .subscribe(
            data => {

              this.messageService.add({ severity: 'success', summary: 'Success', detail: "Deleted Succcessfully!" });
              this.getResources();
            },

            error => {

              if (error.error.ERROR_MSG == null) {

                this.messageService.add({ severity: 'error', summary: 'Error',detail: error.error});

                return;

              }

            });

      },
      reject: () => {

        this.messageService.add({ severity: 'warn', summary: 'Warn', detail: 'You have cancelled Delete Request' });

      }

    });

  }
  editFunction(id){


    this.router.navigate(['edit-resources', id]);

  }

}
