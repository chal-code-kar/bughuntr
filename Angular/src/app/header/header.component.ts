import { Component, OnInit, Input, Inject } from '@angular/core';
import { Globals } from '../globals';
import { IndexService } from '../index.service';
import { environment } from '../../environments/environment';
import { DOCUMENT } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
 
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  empid;
  theme = 'darkTheme';
  icon = "pi pi-moon";
  themeTooltip = "Switch to Dark Theme";
  flag: boolean = false;
  headers: any;
  env = environment.apiURL;
  logout: any;
  logoutApp: any;
  headerText: any;
 
 
  research: any;
  visiblePopup;
  profilePic = "N1";
  srno: any;
  isResearcher = false;
 
  @Input() userInformation: any;
  data1: { header: string; };
  constructor(private service: IndexService,
    private globals: Globals,
    @Inject(DOCUMENT) private document: Document,
    private messageService: MessageService,
    private http: HttpClient,private router:Router) { }
 
  ngOnInit () {
 
  this.globals.brandingRef = this.userInformation["ID"];
    const emp_id = this.userInformation["ID"];
    this.empid = emp_id;
    this.globals.roles = this.userInformation["roles"];
    this.globals.profile = this.userInformation["profile"];
    this.profilePic = this.globals.profile.split("-")[0];
     this.srno = this.globals.profile.split("-")[1];
    
    var roles;
    if (typeof this.globals.roles != 'string'){
      roles = JSON.stringify(this.globals.roles);
    }else{
      roles = this.globals.roles;
    }
 
    if(roles.includes("ROLE_Researcher"))
    {
      this.isResearcher=true
    }
 
    this.http.get<any>(environment.apiURL + 'researchers').subscribe(response => {
 
      this.research = response.data
 
 
 
    });
    this.logout = environment.baseUrl + `/api/logout`;
    this.logoutApp = environment.baseUrl + `/api/logoutApp`;
 
  }
 
  LogoutApi(){
     
  this.http.post(this.logout, null).subscribe({
  next: async (response) => {
    await this.router.navigateByUrl('/login', { replaceUrl: true });
    window.location.reload();
  },
  error: (err) => {
  }
});
 
  }
  flagChange () {
    this.flag = !this.flag;
  }
 
  viewChange () {
    this.globals.view = !this.globals.view;
  }
 
 
 
  switchTheme (theme) {
 
    if (this.theme == 'lightTheme')
    {
      this.theme = 'darkTheme';
      this.icon = "pi pi-moon";
      this.themeTooltip = "Switch to Dark Theme";
    } else
    {
      this.theme = 'lightTheme';
      this.icon = "pi pi-sun";
      this.themeTooltip = "Switch to Light Theme";
    }
    let themeLink = this.document.getElementById('app-theme') as HTMLLinkElement;
 
    if (themeLink)
    {
      themeLink.href = theme + '.css';
    }
  }
 
 
 
  signout () {
    this.http.post(environment.url + '/signout', null, { responseType: "text" }).subscribe(response => {
      this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Signed Out Successfully' });
      window.location.reload();
    }, error => {
      this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Signed Out Successfully' });
      window.location.reload();
    })
  }
}