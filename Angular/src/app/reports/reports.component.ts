import { DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit {

  reportData;
  globalFilter=[];
  cols = [
    { field: 'avatar', header: 'Avatar' },
    { field: 'bprogramme_name', header: 'Program Name' },
    { field: 'title', header: 'Vulnerability Title' },
    { field: 'createddt', header: 'Reported Date' },
    { field: 'modifieddt', header: 'Updated Date' },
    { field: 'rejecteddt', header: 'Approved / Rejected Date' },
    { field: 'rewardeddt', header: 'Rewarded Date' }
  ];

  selectedCols = [
    { field: 'avatar', header: 'Avatar' },
    { field: 'bprogramme_name', header: 'Program Name' },
    { field: 'title', header: 'Vulnerability Title' },
    { field: 'createddt', header: 'Reported Date' },
    { field: 'modifieddt', header: 'Updated Date' },
    { field: 'rejecteddt', header: 'Approved / Rejected Date' },
    { field: 'rewardeddt', header: 'Rewarded Date' }
  ];

  constructor(private http:HttpClient, 
    private messageService:MessageService, 
    private router:Router,
    public datepipe: DatePipe) { }

  ngOnInit() {
    this.onChange();
    this.http.get(environment.apiURL + 'reports').subscribe(response => {
      this.reportData = response;
      if(this.reportData.length == 0){
        this.messageService.add({ severity: 'success', detail: 'No Reports Founds'});
        this.router.navigate(["/"]);
      }

      for(var i=0;i<this.reportData.length;i++){
        var keys = Object.keys(this.reportData[i]);
        for(var j= 0; j<keys.length; j++){
          if(keys[j] == 'createddt' || keys[j] == 'modifieddt' || keys[j] == 'rejecteddt' || keys[j] == 'rewardeddt'){
            if(this.reportData[i][keys[j]] != null){
              this.reportData[i][keys[j]] = this.datepipe.transform(this.reportData[i][keys[j]], 'yyyy-MM-dd');
            }
          }
        }
      }
    });
  }

  onChange(){
    this.globalFilter = [];
    for(var i=0;i<this.selectedCols.length;i++){
      this.globalFilter.push(this.selectedCols[i].field);
    }
  }

}
