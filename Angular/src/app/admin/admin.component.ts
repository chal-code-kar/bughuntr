import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as Highcharts from 'highcharts';
import { MessageService } from 'primeng/api';
import { connectableObservableDescriptor } from 'rxjs/internal/observable/ConnectableObservable';
import { environment } from '../../environments/environment';
import wordCloud from "highcharts/modules/wordcloud.js";

wordCloud(Highcharts);
@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  totalRoles = [];
  uniqueIDs = [];
  UserRole = [];
  dashboardcount;
  querydata;
  query=[];
  help=[];
  number=0;
  Highcharts: typeof Highcharts = Highcharts;
  chartOptions: Highcharts.Options = {
    series: [{
     turboThreshold:1000,
      type: 'wordcloud'
    }]

  };

  constructor(private http: HttpClient,  private messageService: MessageService, private router:Router) { }

  ngOnInit(): void {
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
   
     if(error.error == null && error.status != 403){
      this.messageService.add({ severity: 'error', detail: "An error has occured please try again or contact administrator" });
     }else if (error.status == 403){
      this.messageService.add({ severity: 'error', detail: "Access Denied" });
     }else{
      this.messageService.add({ severity: 'error', detail: error.error });
     }
     
      this.router.navigate(["/"]);
    });
    this.http.get<any>(environment.apiURL + 'wordcloud').subscribe(response => {
      var data=response.content;
      data.sort(function(a, b){return b.count - a.count});
      var main =[]
      for(var i=0;i<5;i++){
        main.push({name:data[i].word,weight:data[i].count,color:'#'+(0x1000000+(Math.random())*0xffffff).toString(16).substr(1,6)});
      }
      this.help=main;
      this.loadgraph();
    });
    this.http.get<any>(environment.apiURL + 'AllQuery').subscribe(response => {
      this.querydata = response;
      for(var i=0;i<this.querydata.length;i++){

        if(this.querydata[i].answer==null){

          this.number=this.number+1;

        }

        if(this.query.length<4){

        if(this.querydata[i].answer==null){

          this.query.push(this.querydata[i])

        }

      }

      }
    });

    this.http.get(environment.apiURL + 'dashboards').subscribe(response => {
      this.dashboardcount = response;
    });


  }
  loadgraph()
  {
    this.chartOptions  = {
      series: [{
        turboThreshold:1000,
        minFontSize:8,
        maxFontSize:15,
        type: 'wordcloud',
        data: this.help,
        name: 'Searched',
        rotation: {
          from: 0,
          to: 0
        },
        spiral: 'rectangular',
        point:{
          events:{
            click: function(e) {
            }.bind(this)
          }
        },

        states:{
          hover:{
            brightness:0.1,
            color:"black"
          }
        }
      }],
      credits: {
        enabled: false
      },
      legend: {
        enabled:true
      },
      tooltip:{
        enabled:true

      },
      title:{
        text:''
      }
    }
  }

}
