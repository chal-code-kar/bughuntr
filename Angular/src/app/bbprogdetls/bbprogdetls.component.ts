import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';



@Component({
  selector: 'app-bbprogdetls',
  templateUrl: './bbprogdetls.component.html',
  styleUrls: ['./bbprogdetls.component.css']
})
export class BbprogdetlsComponent implements OnInit {

  env = environment.apiURL;
  chats: any;
  message;
  researchers: any;
  leaderboard=[];
  constructor(private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private messageService: MessageService,
    private tssvcommonvalidatorService : TssvcommonvalidatorService,
    private confirmationService: ConfirmationService) {
    this.route.params.subscribe(params => this.srno = params.srno);
  }

  announcement;
  displaySuspend = false;
  revokeTill;
  announcementDates;

  researcher_id;
  oldStatus;
  status;

  srno: any;
  active4 = 0;
  project: any;
  data: any;
  Statistics: any;
  researcher_profile_id: any;
  inscope: any[] = [];
  outscope: any[] = [];
  rulecat: any;
  chatTemp;
  
  ruleoptions = [
    { name: 'Rewards', code: 'Rewards' },
    { name: 'Behaviour', code: 'Behaviour' },
    { name: 'Disclosure', code: 'Disclosure' },
    { name: 'General', code: 'General' },
  ];
  rules_category;
  showLeaderBoard = false;

  isAdmin: Boolean=false;

  ngOnInit(): void {

    this.isAdmin = this.tssvcommonvalidatorService.isAdmin();

    this.project = {}
    this.getProjectDetails();
    this.http.get(environment.apiURL + `ProgramsStatistics/` + this.srno).subscribe(response => {
      this.Statistics = response;
      for (var i = 0; i < this.Statistics.researcherjoined.length; i++) {
        if (this.Statistics.researcherjoined[i].sum != null) {
          this.showLeaderBoard = true;
          this.leaderboard.push(this.Statistics.researcherjoined[i]);
        }
      }
      this.leaderboard.sort(function(a, b){return b.sum - a.sum});
    });
    
  }

  getProjectDetails() {
    this.http.get(environment.apiURL + `programs/` + this.srno).subscribe(response => {
      this.project = response;
      this.inscope = this.project.scope[0].inscope.split(':==:');
      this.outscope = this.project.scope[0].outscope.split(':==:');
      this.rules_category = Object.keys(this.project.rules);

      const groupBy = (array, key) => {
        return array.reduce((result, currentItem) => {
          (result[currentItem[key]] = result[currentItem[key]] || []).push(currentItem);
          return result;
        }, {}); 
      };


      for (var i = 0; i < this.project.Announcement.length; i++) {
        this.project.Announcement[i].createddate = this.convert(new Date(this.project.Announcement[i].createddate))
      }

      this.announcement = groupBy(this.project.Announcement, "createddate");
      this.announcementDates = Object.keys(this.announcement);

    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error.error });
      this.router.navigate(["/ProgramDetails"]);
    })
  }

  convert(str) {
    var date = new Date(str),
      mnth = ("0" + (date.getMonth() + 1)).slice(-2),
      day = ("0" + date.getDate()).slice(-2);
    return [date.getFullYear(), mnth, day].join("-");
  }

  joinProgramme() {

    this.http.get(environment.apiURL + `isResearcher`).subscribe(response => {
      if (response) {
        this.data = {
          project_id: this.srno
        }
        this.http.post(environment.apiURL + `joinProgram`, { project_id: this.srno }, { responseType: 'text' }).subscribe(response => {
          this.getProjectDetails();
          this.messageService.add({ severity: 'success', detail: response });
          this.http.get(environment.apiURL + `ProgramsStatistics/` + this.srno).subscribe(response => {
            this.Statistics = response;
            for (var i = 0; i < this.Statistics.researcherjoined.length; i++) {
              if (this.Statistics.researcherjoined[i].sum != null) {
                this.showLeaderBoard = true;
              }
            }
          });
        }, error => {
          this.messageService.add({ severity: 'error', detail: error.error });
        });
      }
      else {
        this.messageService.add({ severity: 'error', detail: "Visitor cannot join Program !" });
      }


    });
  }

  suspend() {
    this.http.post(environment.apiURL + `updatejoinProgram`, { project_id: this.srno, old_status: this.oldStatus, status: 'Suspend', reseacher_id: this.researcher_id, suspendTill: this.revokeTill }, { responseType: 'text' }).subscribe(response => {
      this.displaySuspend = false;
      this.getProjectDetails();
      this.messageService.add({ severity: 'success', detail: response });
      this.http.get(environment.apiURL + `ProgramsStatistics/` + this.srno).subscribe(response => {
        this.Statistics = response;
        for (var i = 0; i < this.Statistics.researcherjoined.length; i++) {
          if (this.Statistics.researcherjoined[i].sum != null) {
            this.showLeaderBoard = true;
          }
        }
      });
    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error });
    });
  }

  unjoin() {
    this.http.post(environment.apiURL + `joinProgram`, { project_id: this.srno, old_status: 'Joined', status: 'UnJoin' }, { responseType: 'text' }).subscribe(response => {
      this.getProjectDetails();
      this.messageService.add({ severity: 'success', detail: response });
      this.http.get(environment.apiURL + `ProgramsStatistics/` + this.srno).subscribe(response => {
        this.Statistics = response;
        for (var i = 0; i < this.Statistics.researcherjoined.length; i++) {
          if (this.Statistics.researcherjoined[i].sum != null) {
            this.showLeaderBoard = true;
          }
        }
      });
    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error });
    });
  }


  changeStatus(researcher_id, oldStatus, status) {
    this.http.post(environment.apiURL + `updatejoinProgram`, { project_id: this.srno, old_status: oldStatus, status: status, reseacher_id: researcher_id }, { responseType: 'text' }).subscribe(response => {
      this.getProjectDetails();
      this.messageService.add({ severity: 'success', detail: response });
      this.http.get(environment.apiURL + `ProgramsStatistics/` + this.srno).subscribe(response => {
        this.Statistics = response;
        for (var i = 0; i < this.Statistics.researcherjoined.length; i++) {
          if (this.Statistics.researcherjoined[i].sum != null) {
            this.showLeaderBoard = true;
          }
        }
      });
    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error });
    });
  }

  getChats() {

    this.http.get(environment.apiURL + 'getChats/' + this.srno).subscribe(response => {
      this.chats = response
      this.chatTemp = response
      this.http.get<any>(environment.apiURL + 'researchers').subscribe(response => {
        this.researchers = response.data
        var arr = [];
        for (var i = 0; i < this.chatTemp.content.length; i++) {
          var item = { id: Number, emp_id: Number, avatar_name: '', message: '', datetime: Date, profile_pic: '' };
          for (var j = 0; j < this.researchers.length; j++) {
            if (this.chatTemp.content[i].emp_id == this.researchers[j].emp_id) {
              item.profile_pic = this.researchers[j].profile_pic
              item.id = this.chatTemp.content[i].srno;
              item.emp_id = this.chatTemp.content[i].emp_id;
              item.avatar_name = this.chatTemp.content[i].avatar_name;
              item.message = this.chatTemp.content[i].message;
              item.datetime = this.chatTemp.content[i].datetime;
            }
          }
          arr.push(item);
        }
        this.chats = arr

      });

    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error });
    });
  }
  postChat() {
    var data = {
      message: this.message,
      project_id: this.srno
    }
    this.http.post(environment.apiURL + 'postChat', data, { responseType: 'text' }).subscribe(response => {
      this.message = null;
      this.getChats();
      this.messageService.add({ severity: 'success', detail: response });
    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error });
    });
  }
  assignValue(researcher_id, oldStatus, status) {
    this.researcher_id = researcher_id;
    this.oldStatus = oldStatus;
    this.status = status;
  }

}
