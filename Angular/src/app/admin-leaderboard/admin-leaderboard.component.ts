import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Router } from '@angular/router';
import { Globals } from '../globals';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';

@Component({
  selector: 'app-admin-leaderboard',
  templateUrl: './admin-leaderboard.component.html',
  styleUrls: ['./admin-leaderboard.component.scss']
})
export class AdminLeaderboardComponent implements OnInit {
  announcements: any;
  research: any;
  leaderboard: any;

  constructor(private http: HttpClient,
    private router:Router,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,
    private globals:Globals) { }

  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)

    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }
    this.tssvcommonvalidatorService.isAccessible();

    this.http.get<any>(environment.apiURL + 'researchers').subscribe(response => {
      this.research = response.data;
      var leaderboard = this.research.slice(0,5);
      for(var i=0;i<leaderboard.length;i++){
        if(leaderboard[i].total_bounty_earned != null && this.leaderboard.length < 6){
          this.leaderboard.push(leaderboard[i]);
        }
      }
    });
  }

}
