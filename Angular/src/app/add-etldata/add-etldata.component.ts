import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';


@Component({
  selector: 'app-add-etldata',
  templateUrl: './add-etldata.component.html',
 styleUrls: ['./add-etldata.component.scss']
})
export class AddEtldataComponent implements OnInit {
 
  employee_number;
  first_name;
  last_name;
  full_name;
  email_address;
  iou_name;
  assignment_id;
  assignment_status;
  base_branch;
  depute_branch;
  country_of_depute;
  base_dc_id;
  per_system_status;
  last_update_date; 
  eai_update_date; 
  announcement;
  
  

  constructor(private http: HttpClient,
    private messageService: MessageService,
    private router: Router,
    private confirmationService: ConfirmationService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService) { }

  ngOnInit(): void {
    this.tssvcommonvalidatorService.isAccessible();
  }


  etlinsert() {
    var data =
    {
      employee_number: this. employee_number ,
      first_name: this.first_name ,
      last_name: this.last_name ,
      full_name: this.full_name ,
      email_address: this.email_address ,
      iou_name: this.iou_name ,
      assignment_id: this.assignment_id ,
      assignment_status: this.assignment_status,
      base_branch: this.base_branch ,
      depute_branch: this.depute_branch ,
      country_of_depute: this.country_of_depute ,
      base_dc_id: this.base_dc_id ,
      per_system_status: this.per_system_status ,
      last_update_date: this.last_update_date ,
      eai_update_date: this.eai_update_date ,
      
      
    }
    if(this.validatetl()) {
    this.http.post(environment.apiURL + 'Postetldata', data, { responseType: 'text' }).subscribe(response => {
    this.announcement=response;
      this.messageService.add({ severity: 'success', detail: response });
       this.router.navigate(['/etl-data']);
    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error });
      this.employee_number='';
      this.full_name='';
      this.iou_name='';
    });

  }
}
  validatetldata() {
      
    var flaggeneralvalidate = false;

    if (this.employee_number == null ) {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter employee number' });
    }
      else if (this.full_name == null || this.full_name.length == 0) {

        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Full name' });
      }
       else if (!this.tssvcommonvalidatorService.isRegexValid("name", this.full_name)) {

        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed .a-zA-Z0-9\s_()&,-]+$/ ' });

       }
    else  if (this.iou_name == null || this.iou_name == ' ') {

       this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter IOU Name' });
     }
     else if (!this.tssvcommonvalidatorService.isRegexValid("name", this.iou_name)) {

       this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed .a-zA-Z0-9\s_()&,-]+$/' });

     }
     else if (this.assignment_id == null ) {

       this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter assignment id' });
     } else if (this.assignment_status == null || this.assignment_status == ' ') {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter assignment status' });
    }
     else if (!this.tssvcommonvalidatorService.isRegexValid("name", this.assignment_status)) {

       this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed .a-zA-Z0-9\s_()&,-]+$/  ' });

     }else if (this.base_dc_id == null ) {

      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter base dc id' });
     }
   
   
   
    else {

      flaggeneralvalidate = true;

    }

    return flaggeneralvalidate;

  }



  validatetl(){
    var flaggeneralvalidate = false;

    if(this.employee_number == null || this.employee_number.length == 0){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter employee number' });
    }else if (!this.tssvcommonvalidatorService.isRegexValid('numeric', this.employee_number)){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter valid employee number' });
    }else if (this.employee_number.length>10){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The size of employee number should be leass than 10 characters' });
    }else if (this.employee_number.length<3){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The size of employee number should be greater than 4 characters' });
    }

    else if(this.full_name == null || this.full_name.length == 0){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Full Name' });
    }else if (!this.tssvcommonvalidatorService.isRegexValid('name', this.full_name)){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed .a-zA-Z0-9\s_()&,- in Full Name' });
    }else if (this.full_name.length>100){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The size of Full Name should be leass than 100 characters' });
    }

    else if(this.iou_name == null || this.iou_name.length == 0){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter IOU Name' });
    }else if (!this.tssvcommonvalidatorService.isRegexValid('name', this.iou_name)){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed .a-zA-Z0-9\s_()&,- in IOU Name' });
    }else if (this.iou_name.length>100){
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The size of IOU Name should be leass than 100 characters' });
    }
    else{
      flaggeneralvalidate = true;
    }
    return flaggeneralvalidate;

  }
}





