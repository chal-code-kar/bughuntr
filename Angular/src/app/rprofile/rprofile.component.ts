import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-rprofile',
  templateUrl: './rprofile.component.html',
  styleUrls: ['./rprofile.component.css']
})
export class RprofileComponent implements OnInit {

  constructor(private http: HttpClient) { }
  research: any
  hunthistory:any
  tags: any
  research2:any[]=[];
  ActualData = [];
  canAdd = true;
  ngOnInit(): void {
    this.http.get<any>(environment.apiURL + 'researchers').subscribe(response => {
      this.research = response.data;
      this.canAdd = response.isResearcher;
      this.ActualData = this.research;
      for (var i = 0; i <= this.research.length; i++) {
        this.tags = this.research[i].skills.split(",")
        this.research[i].tags = this.tags
        if (i <5){
          this.research2.push(this.research[i])
        }
      }
    });

    this.http.get(environment.apiURL + 'huntHistory').subscribe(response => {
      this.hunthistory = response;
    });
  }
 
  onPageChange(e: any) {
    this.research2=[]
    for (var i = 0; i <this.research.length; i++) {
      if (i > e.first-1 && i < e.first + e.rows)
        this.research2.push(this.research[i])
    }
  }

  SearchData(e){
    if(e.length < 5){
      return;
    }
    this.research2 = [];
    this.research = [];
    for(var i=0;i<this.ActualData.length;i++){
      if(this.ActualData[i].avatar.toLowerCase().includes(e.toLowerCase())){
        this.research.push(this.ActualData[i])
      }
    }
    for(var i=0;i<this.research.length;i++){
      if(i<5){
        this.research2.push(this.research[i])
      }
    }
  }


}
