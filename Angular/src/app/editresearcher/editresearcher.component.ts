import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { FileUpload } from 'primeng/fileupload';
import { environment } from '../../environments/environment';

import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';


@Component({
  selector: 'app-editresearcher',
  templateUrl: './editresearcher.component.html',
  styleUrls: ['./editresearcher.component.scss']
})
export class EditresearcherComponent implements OnInit {
  @ViewChild('fubauto') fubauto: FileUpload;
  env = environment.apiURL
  srno: any;
  Name: any;
  Bio: any;
  Team: any;
  data: any;
  tags: any;
  emp_id: number = 0;
  Skills: [] = [];
  getSpecificResearcher: any;
  avatar;
  image_name;
  imageSrc;
  options;
  
  
  teamoptions = [
    { name: 'Corporate PT', code: 'Corporate PT' },
    { name: 'CEG Security', code: 'CEG Security' },
    { name: 'Cyber Security Practice', code: 'Cyber Security Practice' },
    { name: 'Unit SSA', code: 'Unit SSA' },
    { name: 'Others', code: 'Others' },

  ]
  constructor(private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService) {
    this.route.params.subscribe(params => this.srno = params.srno);
  }

  ngOnInit(): void {
    this.http.get(environment.apiURL + `researchers/` + this.srno).subscribe(response => {
      this.getSpecificResearcher = response;
      this.Name = this.getSpecificResearcher.emp_name;
      this.emp_id = this.getSpecificResearcher.emp_id;
      this.Bio = this.getSpecificResearcher.emp_bio;
      this.Skills = this.getSpecificResearcher.skills.split(",");
      this.Team = this.getSpecificResearcher.teams;
      this.avatar = this.getSpecificResearcher.avatar;
      this.image_name=this.getSpecificResearcher.profile_pic;
    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error.error });
      this.router.navigate(["/"]);
    });

    this.http.post(environment.apiURL + 'getAllOptions',"R_SKILLS").subscribe(response => {

      this.options=response;

    

    })
  }

  UpdateResearcher() {
    this.data = {
      srno: this.srno,
      name: this.Name,
      bio: this.Bio,
      id: this.emp_id,
      skills: this.Skills.toString(),
      team: this.Team,
      avatar: this.avatar,
      profile_pic: this.image_name
    }
    if (this.validationforGeneral()) {
      this.http.post(environment.apiURL + `updateresearchers/` + this.srno, this.data, { responseType: 'text' }).subscribe(response => {
        this.router.navigate(['/Profile', this.srno]);
        this.messageService.add({ severity: 'success', detail: response });
        this.upload();
      }, error =>{
        this.messageService.add({ severity: 'error', detail: error.error });
      });
    }
  }

  validationforGeneral() {
    var flaggeneralvalidate = false;
    if (this.avatar.trim().length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please enter valid Avatar' });
    } 
    else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumeric", this.avatar)) {
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in avatar 0-9 a-z A-Z)' });
    } else
     if (this.Bio == null || this.Bio.trim().length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please enter Proper Bio' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.Bio)) {
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in description (a-z A-Z 0-9 # \- _ * = . + : ! \[ \] ( ) <newLine> , & \' \" \| \/ ? \\ % ^ ` ; ~ $ @)' });
    } else if (this.Team == null || this.Team.trim().length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please select team details' });
    } else if (this.Skills.toString() == null || this.Skills.toString().trim() == '') {
      this.messageService.add({ severity: 'error', detail: 'Please select skills' });
    }else if(this.image_name == null){
      this.messageService.add({ severity: 'error', detail: 'Please select Profile picture' });
    }else if(!this.tssvcommonvalidatorService.isRegexValid("alphanumeric", this.image_name)){
      this.messageService.add({ severity: 'error', detail: 'Please select Profile picture' });
    } else {
      flaggeneralvalidate = true;
    }
    return flaggeneralvalidate;
  }

  onFileSelected(event) {
    if (event.currentFiles.length > 0) {
      var file = event.currentFiles[0];
      if (file == null) {
        this.messageService.add({ severity: 'error', detail: "Please choose a valid picture" });
        this.fubauto.clear();
        return;
      } else if (file.name.length == 0 || !this.tssvcommonvalidatorService.isRegexValid("filename", file.name)) {
        this.messageService.add({ severity: 'error', detail: 'Please enter File with valid Filename' });
        this.fubauto.clear();
        return;
      } else if (!file.name.endsWith(".png") && !file.name.endsWith(".jpeg")) {
        this.messageService.add({ severity: 'error', detail: 'Please enter valid File with PNG/JPEG Extension' });
        this.fubauto.clear();
        return;
      }
      this.image_name = event.currentFiles[0];

      const reader = new FileReader();
      reader.readAsDataURL(this.image_name);
      this.imageSrc = reader.result as string;
      reader.onload = (_event) => {
        this.imageSrc = reader.result;
      }
    }
  }

  upload() {
    var fd = new FormData();
    fd.append('file', this.image_name);
    var file = this.image_name;
    if (file == null) {
      return;
    } else if (file.name.length == 0 || !this.tssvcommonvalidatorService.isRegexValid("filename", file.name)) {
      this.messageService.add({ severity: 'error', detail: 'Please enter valid File' });
      return;
    }
    this.http.post(environment.apiURL + 'upload/' + this.srno, fd, {responseType:'text'}).subscribe(response => {
      this.messageService.add({ severity: 'success', detail: response });
    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error });
    });
  }

  deleteImage(){
    this.image_name = null;
    this.fubauto.clear();
    this.imageSrc = null
  }

  image(e)

  {

    this.image_name=e

    e.style.border = "5px solid blue";

  }
}
