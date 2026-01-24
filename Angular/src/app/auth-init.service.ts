import { HttpClient } from "@angular/common/http";
import { Globals } from "./globals";
import { Injectable } from "@angular/core";
import { IndexService } from "./index.service";

@Injectable({ providedIn: 'root' })
export class AuthInitService {
 

    userInfo: any;

  constructor(
    private http: HttpClient,
    private globals:Globals,
    private service: IndexService
  ) {}
 
  loadRole(): Promise<void> {
    return new Promise((resolve) => {
      this.service.getMenus().subscribe({
        next: (data:any) => {
        //   this.authService.setRole(res.role);

          this.userInfo = data.body.brandingDetails;
          this.userInfo.roles = data.body.roles;
          this.userInfo.profile=data.body.profile;

          this.globals.roles = data.body.roles;
          this.globals.roles=this.userInfo.roles;
          this.globals.profile=this.userInfo.profile;
          
          resolve();
        },
        error: () => {
        //   this.authService.setRole(null);
          resolve();
        }
      });
    });
  }
}
 