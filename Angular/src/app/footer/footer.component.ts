import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { IndexService } from '../index.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  ThirdPartyNotices = false;
  TermsOfUse = false;
  displayBrowser = false;
  displayPopUp = false;
  version = 1.5;
  ref: DynamicDialogRef;

  constructor(private http: HttpClient,
    public dialogService: DialogService,
    public service:IndexService) { }

  components = [
    { "component": "Roboto Font", "license": "Apache license, version 2.0", "link": "https://www.apache.org/licenses" }
  ];
  
  url = environment.apiURL + '/logo/TCS_Gradient_White_2021_V2.png';

  ngOnInit(): void {
    this.service.getVersion().subscribe((data: any) => {
        var versions = data.body;
        this.version = versions["version"];
      }
    )
  }

  setBrowserFooterWindowPos() {
    this.displayBrowser = true;
  }
  termofuse() {
    this.TermsOfUse = true;
  }

  functionToCloseDialog() {
    this.TermsOfUse = false;
  }
  thirdpartynotices() {
    this.ThirdPartyNotices = true;
  }

}
