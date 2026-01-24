import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuItem, MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';
import { Globals } from '../globals';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';

@Component({
  selector: 'app-help-update',
  templateUrl: './help-update.component.html',
  styleUrls: ['./help-update.component.scss']
})
export class HelpUpdateComponent implements OnInit {
  srno: any;
  blogs: any;
  data:any;

  constructor(private router: Router,
    private http: HttpClient,
    private route: ActivatedRoute,
    private globals:Globals,
    private messageService: MessageService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService) {
     }

  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)
    this.srno = this.route.snapshot.params['srno'];
    this.http.get<any>(environment.apiURL + 'blogs').subscribe(response => {
      this.blogs=response.content;
      for(var i=0;i<this.blogs.length;i++){
        if(this.srno==this.blogs[i].srno){
          this.data=this.blogs[i]
        }
      }

    });

}
}
