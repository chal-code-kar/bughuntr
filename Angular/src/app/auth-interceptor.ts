import { environment } from './../environments/environment';
import { CookieService } from 'ngx-cookie-service';
import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';
import 'rxjs/add/operator/do';
import { Globals } from './globals';
import { map } from 'rxjs/operators';
import $ from "jquery";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private router: Router, private globals: Globals, private cookieService: CookieService) { }
  httpHeaders;
  
  
  
  
  
  
  


  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let requestToForward = request;

    
    

    
    
    
    
    
    
    
    if (this.globals.empId != null) {
      requestToForward = requestToForward.clone({ headers: requestToForward.headers.set("USER_ID", this.globals.empId) })
    }


    return next.handle(requestToForward).pipe(map((event: HttpEvent<any>) => {
      



      if (event instanceof HttpResponse) {
        this.httpHeaders = event;

        if (this.httpHeaders.status == 404 || this.httpHeaders.status == '404') {
          
          this.router.navigate(['/']);
        }

        
        
        
        

        if (event.body !== null && event.body.hasOwnProperty('text')) {
          if (event.body.text === 'SM_USER_CHANGE' || event.body.text === 'SESSION_EXPIRED') {
            location.href = "index.html";
            return;
          }

          if (event.body.text === 'SM_USER_NULL' || event.body.text === 'SM_KEY_ERROR') {
            this.router.navigate(['/error'], { queryParams: { errorType: "SMerror" } });
          }
          if (event.body.text === 'ACCESS_DENIED' || event.body.text === 'NO_CONTENT') {
            this.router.navigate(['/403'], { queryParams: { errorType: "accessDenied" } });
          }

          if (event.body.text === 'USER_NULL') {
            window.location.href = "login.html";
          }
        }

        if (event.headers.get('str_glob') !== this.globals.brandingRef && this.globals.brandingRef !== "NOT_SET" && event.headers.get('str_glob') !== null) {
          location.href = "index.html";
          return;
        }

        return event;
      }
    })).catch((err) => {

      if (err.status === 401 && err.error == "ACCESS DENIED") {
        
        this.router.navigate(['/login']);


      } else if (err.status === 403 || err.statusText === 'Forbidden') {
        this.router.navigate(['/403'], { queryParams: { errorType: "accessDenied" } });
      }
      return Observable.throw(err);
    });
  }
}


