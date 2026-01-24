import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import {BreadcrumbModule} from 'primeng/breadcrumb';

@Component({
	selector: 'app-history',
	templateUrl: './history.component.html',
	styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
     data;
	
	announcements;
	constructor(private http: HttpClient  ) { }

	ngOnInit(): void {

		this.http.get<any>(environment.apiURL + 'history',{ observe: 'response' }).subscribe(response => {

			this.announcements = response.body;	
		  });
	}
}
