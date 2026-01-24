import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationErrorHandler } from './ApplicationErrorHandler';
import { environment } from '../environments/environment';
import { Subject, Observable } from 'rxjs';
 

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class IndexService {

  
  menuURL= environment.url + '/menu';

  scriptURL= environment.url +  '/getScript';

  buildNoUrl : string =  environment.url + "/getversion";

  authUrl=environment.url+"/isAuthUser"
  
  constructor(private http: HttpClient, private applicationErrorHandler: ApplicationErrorHandler ) { }

  getMenus() {
    return this.http.get(this.menuURL,{ observe: 'response' }).map(response => response).catch(this.applicationErrorHandler.handleError);
  }

  getScript() {
    return this.http.get(this.scriptURL,{ observe: 'response' }).map(response => response).catch(this.applicationErrorHandler.handleError);
  }
  getVersion() {
    return this.http.get(this.buildNoUrl,{ observe: 'response' }).map(response => response).catch(this.applicationErrorHandler.handleError);
  }
   isAuthUser(){
    return this.http.get(this.authUrl,{ observe: 'response', responseType: 'text' }).map(response => response).catch(this.applicationErrorHandler.handleError);
  }
  
  
  
  
  

}
