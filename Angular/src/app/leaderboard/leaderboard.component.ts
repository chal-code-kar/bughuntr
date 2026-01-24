import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.scss']
})
export class LeaderboardComponent implements OnInit {
  env = environment.apiURL
  project:any[]=[];
  constructor(private http: HttpClient) { }
  research:any;
  leaderboard = [];
  dashboardcount:any;
  dashbrd=false;

  

  ngOnInit(): void {
  

  this.http.get<any>(environment.apiURL + 'researchers').subscribe(response => {
    this.research = response.data;
    var leaderboard = this.research.slice(0,5);
    leaderboard.sort((a, b) => (a.points < b.points) ? 1 : -1);
    
    for(var i=0;i<leaderboard.length;i++){
      if(leaderboard[i].points != null && this.leaderboard.length < 6){
        this.leaderboard.push(leaderboard[i]);
      }
    }
  });
  


}
}
