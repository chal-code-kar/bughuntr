import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-drop-down-table',
  templateUrl: './drop-down-table.component.html',
  styleUrls: ['./drop-down-table.component.scss']
})
export class DropDownTableComponent implements OnInit {

  data;

  constructor(private messageService: MessageService,
    private http: HttpClient,
    private router: Router,) { }

  ngOnInit(): void {
    this.http.get<any>(environment.apiURL + 'GetDropdown' ).subscribe(response => {
      this.data= response;
    });


  }

}
