import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { IndexService } from '../index.service';
 
@Component({
  selector: 'app-bb',
  templateUrl: './bb.component.html',
  styleUrls: ['./bb.component.css']
})
export class BbComponent implements OnInit {
  blogs: any;
  blogtemp: any;
  env = environment.apiURL
  constructor(private http: HttpClient,private service :IndexService) { }
  project:any[]=[];
  projectcount:any[]=[];
  researchercount:any[]=[];
  projectcountlastweek:any[]=[]
  research:any;
  dashboardcount:any;
  leaderboard = [];
  job:any;
  imageSrc;
  dashbrd=false;
  showB=true;
  isR;
  isAuthentic=false;
 
  ngOnInit(): void {
 
    this.service.isAuthUser().subscribe(()=>{
        this.http.get<any>(environment.apiURL + 'WeeklyPrograms').subscribe(response => {
        this.project = response.programs;
      })
 
      this.http.get<any>(environment.apiURL + 'researchers').subscribe(response => {
        this.research = response.data;
        this.isR=response.isResearcher;
       
        var leaderboard = this.research.slice(0,5);
        leaderboard.sort((a, b) => (a.points < b.points) ? 1 : -1);
        for(var i=0;i<leaderboard.length;i++){
          if(leaderboard[i].points != null && this.leaderboard.length < 6){
            this.leaderboard.push(leaderboard[i]);
          }
        }
      });
 
      this.leaderboard.sort((a,b) => {
        return a.points - b.points;
      });
     
     
      this.http.get(environment.apiURL + 'dashboards').subscribe(response => {
        this.dashboardcount = response;
        if(this.dashboardcount.totalprogram>0 && this.dashboardcount.totalresearchers>0 && this.dashboardcount.totalbounty>0 && this.dashboardcount.bugslastquarter>0){
          this.dashbrd=true
        }
      });
      this.http.get<any>(environment.apiURL + 'blogs').subscribe(response => {
        this.blogs=response.content;
        if(this.blogs.length>3){
          this.blogs.sort((a, b) => b.createdate - a.createdate)
          this.blogtemp=this.blogs.slice(0,3)
        }else{
          this.showB=false;
        }
 
      });
      });
  }
}