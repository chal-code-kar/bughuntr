import { Globals } from './globals';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';

@Injectable()
export class AdminAuthGuard  {
isAuthorized: boolean = false;
  constructor(protected router: Router, protected globals: Globals) { }
  canActivate() {
    this.globals.roles.forEach(role => {
      if(role=='ROLE_ADMIN' || role=='ROLE_Administrator'){
        this.isAuthorized= true;
      }
    });

    if(this.isAuthorized) return true;

    this.router.navigate(['/error'], { queryParams: { errorType: "accessDenied" } });
    return false;
  }
}