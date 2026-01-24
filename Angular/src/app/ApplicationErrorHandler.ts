import {ErrorHandler, Injectable, Injector} from '@angular/core';
import 'rxjs/add/observable/throw';
import { Observable } from 'rxjs/Observable';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class ApplicationErrorHandler extends ErrorHandler {
    handleError(responseError: any) {
        if (responseError instanceof HttpErrorResponse) {

        

        if(typeof(responseError.url)=="string" && responseError.url.indexOf('/utxLogin/login') > -1)
        {
            location.href = "index.html";
        }
        
         else if(typeof( responseError.error.text)=="string" && responseError.error.text.indexOf('/utxLogin/login') > -1)
          {
              location.href = "index.html";
            }
    }

    else {
        let errorMsg: string = responseError.toString();
        if(errorMsg.indexOf('Error: Loading chunk') >= 0){
            location.href = "index.html";
        }


    }
    return Observable.throw('Throwing response Error',responseError || 'Server Error');
}
}
