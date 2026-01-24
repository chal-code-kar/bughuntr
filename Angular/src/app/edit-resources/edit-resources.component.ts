import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';


@Component({
  selector: 'app-edit-resources',
  templateUrl: './edit-resources.component.html',
  styleUrls: ['./edit-resources.component.scss']
})
export class EditResourcesComponent implements OnInit {
  resources;
  srno;
  constructor(private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,

    private confirmationService: ConfirmationService) { }

  ngOnInit(): void {
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

        this.http.get(environment.apiURL + 'deleteResources/' + id, { responseType: 'text' })
          .subscribe(
            data => {

              this.messageService.add({ severity: 'success', summary: 'Success', detail: "Deleted Succcessfully!" });
              this.getResources();
            },

            error => {

              if (error.error.ERROR_MSG == null) {

                this.messageService.add({ severity: 'error', summary: 'Error',detail: error.error });

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
