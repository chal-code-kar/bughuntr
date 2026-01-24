import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-view-researcher',
  templateUrl: './view-researcher.component.html',
  styleUrls: ['./view-researcher.component.scss']
})
export class ViewResearcherComponent implements OnInit {

  env = environment.apiURL;
  constructor(private http: HttpClient, private route: ActivatedRoute, private messageService: MessageService, private router: Router) {
    this.route.params.subscribe(params => this.srno = params.srno);
  }
  srno: any;
  getSpecificResearcher: any
  tags: any;
  detailhunthistory: any;
  counter = 10;
  flag = false;

  Tcritical=0;

  Thigh=0;

  Tmedium=0;

  Tlow=0;

  Acritical=0;

  Ahigh=0;

  Amed=0;

  Alow=0;

  valid;
  ngOnInit(): void {
    this.http.get(environment.apiURL + `researchers/` + this.srno).subscribe(response => {
      this.getSpecificResearcher = response;
      this.detailhunthistory = this.getSpecificResearcher.detailed_particular_huntHistory;

      var value=0;

      for(var i=0;i<this.detailhunthistory.length;i++){

        var cvss=this.detailhunthistory[i].cvss_score

        if(cvss >= 0.1 && cvss<3.9){

          this.Tlow++

        }else if(cvss>=4.0 && cvss<6.9){

          this.Tmedium++

        }else if(cvss>=7.0 && cvss<8.9){

          this.Thigh++

        }else if(cvss>=9.0 && cvss<10.0){

          this.Tcritical++

        }



        if(this.detailhunthistory[i].status=="Approved"){

          value=value+1

          if(cvss >= 0.1 && cvss<3.9){

            this.Alow++

          }else if(cvss>=4.0 && cvss<6.9){

            this.Amed++

          }else if(cvss>=7.0 && cvss<8.9){

            this.Ahigh++

          }else if(cvss>=9.0 && cvss<10.0){

            this.Acritical++

          }

        }

      }

      this.valid=Math.round(((value/this.detailhunthistory.length)*100));

      this.tags = this.getSpecificResearcher.skills.split(",")

      this.getSpecificResearcher.tags = this.tags;

      
    }, error => {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error.error });
      this.router.navigate(["/"]);
    });
  }
  setCounter() {
    this.counter = Number(this.counter);
  }
  showdetails() {
    this.flag = true;
  }


}
