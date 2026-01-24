import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { FileUpload } from 'primeng/fileupload';
import { environment } from '../../environments/environment';

import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';


@Component({
  selector: 'app-add-researcher',
  templateUrl: './add-researcher.component.html',
  styleUrls: ['./add-researcher.component.scss'],
  providers: [MessageService]
})
export class AddResearcherComponent implements OnInit {
  @ViewChild('fubauto') fubauto: FileUpload;

  image_name:File;
  Name: String = '';
  Bio: String = '';
  Team: String ='';
  Skills: [] = [];
  uploading : Boolean = false;
  avatar :String = '';
  imageSrc;
  avatarAvailable;
  options;

  teamoptions;
  NewImage: any;
  data: any;
  displayAvailableButton = true;
  id:any;
  Allowed:any;
  menuData = {
    "userInfo": {
      "firstName": {},
      "lastName": {},
      "ID": {},
      "icalmsRole": {}
    },
    "username": {}
  };

  constructor(private router: Router, private http: HttpClient, private messageService: MessageService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,) { }


  ngOnInit(): void {
    this.http.get<any[]>(environment.url + `/menu`).subscribe(
      data => {
        this.menuData.userInfo = data["brandingDetails"];
        this.menuData.username = this.menuData.userInfo.firstName + " " + this.menuData.userInfo.lastName;
        this.id=this.menuData.userInfo.ID;
        this.http.get(environment.apiURL + `isallowed/` + this.id).subscribe(data => {
          this.Allowed=data
        });
      }
    );



    this.http.post(environment.apiURL + 'getAllOptions',"R_SKILLS").subscribe(response => {
      this.options=response;
    })


  }

  Submit() {
    if (this.validationforResearcher()) {
    this.data = {
      bio: this.Bio,
      skills: this.Skills.toString(),
      team: this.Team,
      avatar: this.avatar,
       profile_pic: this.image_name
    }
    var fd = new FormData();
    fd.append('data', JSON.stringify(this.data));
      this.http.post(environment.apiURL + `researchers`, fd, { responseType: 'text' }).subscribe(response => {
        this.router.navigate(["/app-rprofile"]);
        this.messageService.add({ severity: 'success', detail: response });
      }, error => {
        this.messageService.add({ severity: 'error', detail: error.error });
      });
    }
  }

  validationforResearcher() {
    var flaggeneralvalidate = false;
    if (this.avatar.trim() == '') {
      this.messageService.add({ severity: 'error', detail: 'Please enter valid Avatar' });
    }else if(!this.tssvcommonvalidatorService.isRegexValid("alphanumeric", this.avatar)){
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in avatar 0-9 a-z A-Z)' });
    } else if (this.Bio.trim() == '') {
      this.messageService.add({ severity: 'error', detail: 'Please enter valid Bio' });
    } else if(!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.Bio)){
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in bio (a-z A-Z 0-9 # \- _ * = . + : ! \[ \] ( ) <newLine> , & \' \" \| \/ ? \\ % ^ ` ; ~ $ @)' });
    } else if (this.Skills.length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please select skills' });
    } 
    else if(this.image_name == null){
      this.messageService.add({ severity: 'error', detail: 'Please select Profile picture' });
    }else if(!this.tssvcommonvalidatorService.isRegexValid("alphanumeric", this.image_name)){
      this.messageService.add({ severity: 'error', detail: 'Please select Profile picture' });
    }
    else {
      flaggeneralvalidate = true;
    }
    return flaggeneralvalidate;
  }

  onFileSelected(event){
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
      } else if (!file.name.endsWith(".png") && !file.name.endsWith(".jpeg") && !file.name.endsWith(".jpg")) {
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

  checkAvailability(){
    this.http.get<any[]>(environment.apiURL + `avatar/` + this.avatar).subscribe(data => {
      this.displayAvailableButton = false;
      this.avatarAvailable = data;
    })
  }
  image(e)

  {
    this.image_name=e
    e.style.border = "5px solid blue";

  }
}
