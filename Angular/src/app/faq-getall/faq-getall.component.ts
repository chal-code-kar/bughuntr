import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';


@Component({
  selector: 'app-faq-getall',
  templateUrl: './faq-getall.component.html',
  styleUrls: ['./faq-getall.component.scss']
})
export class FaqGetallComponent implements OnInit {

  constructor(private http: HttpClient,
    private router: Router, 
    private messageService: MessageService,
    private route: ActivatedRoute,) {this.srno = this.route.snapshot.params['srno']; }
    show;
    srno;
    lst;
  ngOnInit(): void {
    this.Faqshow();
  }
  Faqshow(){
    var lst = [];
    this.http.get<any>(environment.apiURL + 'FaqAllQuery').subscribe(response => {
      this.show=response;
     
    
  })
  }
}
