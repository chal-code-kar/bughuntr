import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';

@Component({
  selector: 'app-bounty-program',
  templateUrl: './bounty-program.component.html',
  styleUrls: ['./bounty-program.component.scss']
})
export class BountyProgramComponent implements OnInit {

  env = environment.apiURL;
  totalProject = [];
  temp = [];
  temp1 = [];
  temp2 = [];
  joinedPrograms: any;

  constructor(private http: HttpClient) { }
  invitedoptions = [
    { name: 'Public', code: 'Public' },
    { name: 'Protected', code: 'Protected' },
    { name: 'Private', code: 'Private' },
  ]
  ptype = "default";
  ptypeselect = "default";
  project: any;
  PrgrammeStatistics: any;
  canCreate: any;
  myPrograms: any;
  emp_id: any;
  bbadmin: any;

  ngOnInit(): void {

  
    this.http.get<any>(environment.apiURL + 'programs').subscribe(response => {
      this.project = response.programs;
      this.bbadmin = response.bbAdmin
      this.canCreate = response.canCreate;
      this.totalProject = this.project;
      this.myPrograms = response.myprograms;
      this.joinedPrograms = response.joinedprograms;
      for (var i = 0; i < this.project.length; i++) {
        if (i < 5) {
          this.temp.push(this.project[i])
        }
      }
      for (var i = 0; i < this.myPrograms.length; i++) {
        if (i < 5) {
          this.temp1.push(this.myPrograms[i])
        }
      }
      for (var i = 0; i < this.joinedPrograms.length; i++) {
        if (i < 5) {
          this.temp2.push(this.joinedPrograms[i])
        }
      }
    });
    this.http.get(environment.apiURL + `ProgramsStatistics/`).subscribe(response => {
      this.PrgrammeStatistics = response;
    });
  }

  SearchData(e: string) {
    this.temp = [];
    this.totalProject = [];
    this.selectType();
    if (e.length > 2) {
      this.temp = [];
      for (var i = 0; i < this.totalProject.length; i++) {
        if (this.totalProject[i].bprogramme_name.toLowerCase().includes(e.toLowerCase())) {
          this.temp.push(this.totalProject[i])
        }
      }
      this.totalProject = this.temp;
      this.temp = this.totalProject.slice(0, 5);
    }
  }
  onPageChange(e: any) {
    this.temp = [];

    for (var i = 0; i < this.totalProject.length; i++) {
      if (i > e.first - 1 && i < e.first + e.rows)
        this.temp.push(this.totalProject[i])
    }
  }
  onPageChange1(e: any) {
    this.temp1 = [];
    for (var i = 0; i < this.myPrograms.length; i++) {
      if (i > e.first - 1 && i < e.first + e.rows)
        this.temp1.push(this.myPrograms[i])
    }

  }
  onPageChange2(e: any) {

    this.temp2 = [];
    for (var i = 0; i < this.joinedPrograms.length; i++) {
      if (i > e.first - 1 && i < e.first + e.rows)
        this.temp2.push(this.joinedPrograms[i])
    }
  }

  selectType() {
    for (var i = 0; i < this.project.length; i++) {
      if (this.project[i].ptype == this.ptype || this.ptype == "default")
        this.totalProject.push(this.project[i])
    }
    this.temp = this.totalProject.slice(0, 5);
  }

}
