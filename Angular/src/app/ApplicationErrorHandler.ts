import {ErrorHandler, Injectable, Injector} from '@angular/core';
import 'rxjs/add/observable/throw';
import { Observable } from 'rxjs/Observable';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable()
export class ApplicationErrorHandler extends ErrorHandler {
    constructor(private injector: Injector) {
        super();
    }

    private navigateToRoot(): void {
        const router = this.injector.get(Router);
        router.navigate(['/']);
    }

    handleError(responseError: any) {
        if (responseError instanceof HttpErrorResponse) {



        if(typeof(responseError.url)=="string" && responseError.url.indexOf('/utxLogin/login') > -1)
        {
            this.navigateToRoot();
        }

         else if(typeof( responseError.error.text)=="string" && responseError.error.text.indexOf('/utxLogin/login') > -1)
          {
              this.navigateToRoot();
            }
    }

    else {
        let errorMsg: string = responseError.toString();
        if(errorMsg.indexOf('Error: Loading chunk') >= 0){
            this.navigateToRoot();
        }


    }
    return Observable.throw('Throwing response Error',responseError || 'Server Error');
}
}
