import { Injectable } from '@angular/core';

@Injectable()
export class Globals {
    brandingRef: string = 'NOT_SET';
    roles: any[]=null;
    view: boolean=false;
    empId=null;
    profile:string = 'N1';
    isAuthenticated = false;
    requestCount = 0;
}
