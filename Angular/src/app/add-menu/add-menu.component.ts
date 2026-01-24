import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';

@Component({
  selector: 'app-add-menu',
  templateUrl: './add-menu.component.html',
  styleUrls: ['./add-menu.component.scss']
})
export class AddMenuComponent implements OnInit {
  name:String="";
  link:String="";
  role=[];
  tagged;
  roles;
  selected:String;

  constructor(private router: Router,private http: HttpClient,
    private messageService: MessageService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService) { }

  ngOnInit(): void {
   
    this.tssvcommonvalidatorService.isAccessible();
    this.http.get<any>(environment.apiURL + 'rolesAndPermission').subscribe(response => {
      this.roles=response.totalRoles;
  }, error => {
    this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error });
  });
  }

  submit(){
     if(this.validate()){
    this.selected=" ";
          for(var i=0;i<this.tagged.length;i++){
              this.selected= this.selected + "," + this.tagged[i];
          }
          this.selected= this.selected.slice(2,this.selected.length);
      var data ={
        menuname:this.name,
        link:this.link,
        role:this.selected
      }
      this.http.post(environment.apiURL + 'addMenu',data, { responseType: 'text' }).subscribe(response => {
          this.messageService.add({severity:'success', detail: response});
          this.router.navigate(["/menus"]);
      },error => {
        this.messageService.add({ severity: 'error', detail: 'Please Enter the Mandatory Details' });
        this.router.navigate(["/"]);
      });
    }
  }

  validate(){
    var flaggeneralvalidate = false;
    if (this.name.length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please enter Menu Name' });
    }else if (!this.tssvcommonvalidatorService.isRegexValid("description", this.name)) {
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in Menu Name \n.a-zA-Z0-9_()&,-:/ <space>' });
    } else if (this.name.length >100) {
      this.messageService.add({ severity: 'error', detail: 'The length of Menu Name should be less than 100 characters' });
    } 

    else if (this.link.length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please enter Link' });
   
    }else if (!this.tssvcommonvalidatorService.isRegexValid("description", this.link)) {
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in Link \n.a-zA-Z0-9_()&,-:/ <space>' });
    } else if (this.link.length >100) {
      this.messageService.add({ severity: 'error', detail: 'The length of Link should be less than 100 characters' });
    } 
    
    else if (!this.tagged || this.tagged == undefined || this.tagged == null || this.tagged.length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please Select Role' });
    }
    else if(this.name.length==0 || this.link.length==0 || !this.tagged || this.tagged == undefined || this.tagged == null || this.tagged.length == 0){
      this.messageService.add({ severity: 'error', detail: 'Please Enter the Mandatory Details' });
    }
    else {
      return true
    }
    return false;
  }



}
