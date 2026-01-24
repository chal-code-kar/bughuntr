import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import { Globals } from '../globals';
@Component({
  selector: 'app-showquery',
  templateUrl: './showquery.component.html',
  styleUrls: ['./showquery.component.scss']
})
export class ShowqueryComponent implements OnInit {
  showdata: any;
  querydialog: any;
  data: any;
  id: any;
  query;
  answer;
  srno;
  listquery;
  listquery1;


  constructor(private http: HttpClient,
    private router: Router,
    private globals:Globals,
    private messageService: MessageService, private tssvcommonvalidatorService: TssvcommonvalidatorService,
  ) { }

  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)
   
    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }

    this.tssvcommonvalidatorService.isAccessible();
    this.showdataquery();
  }

  Save() {

    var editquery =
    {
      answer: this.answer,
      srno: this.srno
    }
    if (this.validationQuery()) {
      this.http.post(environment.apiURL + 'updatequery', editquery, { responseType: 'text' }).subscribe(response => {
        this.data = response;

        this.querydialog = false;
        this.messageService.add({ severity: 'success', summary: 'Success', detail: "Query Answered" });

        this.showdataquery();
      }, error => {
        this.querydialog = false;
        this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error });
      });
    }
  }

  Query(e) {
    this.querydialog = true;
    for (var i = 0; i < this.showdata.length; i++) {
      if (e == this.showdata[i].srno) {
        this.srno = e
        this.query = this.showdata[i].query
        this.answer = null
      }
    }
  }

  hide() {
    this.querydialog = false;
    this.showdata.answerby = null;

  }

  showdataquery() {

    var listquery = [];
    var listquery1 = [];
    this.http.get<any>(environment.apiURL + 'AllQuery').subscribe(response => {

      this.showdata = response;

      for (var i = 0; i < this.showdata.length; i++) {

        if (this.showdata[i].answer == null) {
          listquery.push(this.showdata[i])

        }
        else {

        }
      }
      this.listquery = listquery

      for (var i = 0; i < this.showdata.length; i++) {

        if (this.showdata[i].answer != null) {
          listquery1.push(this.showdata[i])

        }
        else {

        }
      }
      this.listquery1 = listquery1

    })



  }

  validationQuery() {

    var flaggeneralvalidate = false;

    if (this.answer == null || this.answer.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Answer for the Query' });
    } else if(!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.answer)){
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in Answer a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@' });
    }else if(this.answer.length>1000){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The length of answer should be less than 1000 characters' });
    } else {
      flaggeneralvalidate = true;
    }
    return flaggeneralvalidate;
  }
}

