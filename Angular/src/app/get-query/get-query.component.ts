import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';



@Component({
  selector: 'app-get-query',
  templateUrl: './get-query.component.html',
  styleUrls: ['./get-query.component.scss']
})
export class GetQueryComponent implements OnInit {
  data: any;
  srno;
  rname;
  research: any;
  lst;


  constructor(private http: HttpClient,
    private route: ActivatedRoute,
    private messageService: MessageService ) { this.srno = this.route.snapshot.params['srno']; }

 ngOnInit(): void {
 
    this.http.get<any>(environment.url + '/getUserId').subscribe(response=>{
      this.GetQuery(response);
    });
   
  }
 
  GetQuery(response: any) {
 
    var lst = [];
    debugger
    this.http.get<any>(environment.apiURL + 'MyQuery/'+response).subscribe(response => {
      this.data = response;
      this.lst=lst
 
    })
  


  }
}
