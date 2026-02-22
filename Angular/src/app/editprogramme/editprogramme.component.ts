import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Avatar } from 'primeng/avatar';
import { environment } from '../../environments/environment';
import { Globals } from '../globals';

import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';


@Component({
  selector: 'app-editprogramme',
  templateUrl: './editprogramme.component.html',
  styleUrls: ['./editprogramme.component.scss']
})
export class EditprogrammeComponent implements OnInit {

  active4 = 0;
  empid: string = "";
  flaggeneralvalidate = false;
  flagaddjobvalidate = false;
  minDate: Date = new Date();
  apm_id = "";
  allResearcherList;
  constructor(private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private globals: Globals,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService) { }

  myDate = new Date();
  options = [
    { name: 'High', code: 'High' },
    { name: 'Critical', code: 'Critical' },
    { name: 'Medium', code: 'Medium' },
    { name: 'Low', code: 'Low' },
  ]
  vunloptions = [
    { name: 'High', code: 'High' },
    { name: 'Critical', code: 'Critical' },
    { name: 'Medium', code: 'Medium' },
    { name: 'Low', code: 'Low' }
  ]
  invitedoptions = [
    { name: 'Public', code: 'Public' },
    { name: 'Private', code: 'Private' },
    { name: 'Protected', code: 'Protected' }
  ]
  rewardUnit = [
    { name: 'GEMS', code: 'GEMS' },
  ]

  ruleoptions = [
    { name: 'Rewards', code: 'Rewards' },
    { name: 'Behaviour', code: 'Behaviour' },
    { name: 'Disclosure', code: 'Disclosure' },
    { name: 'General', code: 'General' },
  ]

  Announcementptions = [
    { name: 'Rule Clarification', code: 'Rule Clarification' },
    { name: 'Reward', code: 'Rewards' },
    { name: 'Scope Change', code: 'Scope Change' },
    { name: 'General', code: 'General' },
  ]

  
  
  
  
  
  newJob = "";
  newPricing = 0;
  newMaxPricing = 0;
  reward_type = "";
  jobs: any[] = [];

  newCatg = "";
  newrules = "";
  rules: any[] = [];

  newAnnoCat = "";
  newAnnouncement = "";
  announcements: any[] = [];
  UpdatedAnnouncements: any[] = [];

  newinScope = "";
  newoutscope = "";

  program_name: any;
  owner_empid: Number = 0;
  startdate: Date = new Date();
  enddate: Date = new Date();
  api: any;
  technology: any;
  description: any;
  criticality: any;
  unit: any;
  staticpage: any;
  dynamicpage: any;
  roles: any;

  overview: any;
  access: any;
  resources: any;

  inScope: any[] = [];
  outScope: any[] = [];
  project;
  projectid;
  list2 = [];
  researcherList;
  OriginalInvited = [];
  addedjobs = [];

  programStatusFlg = true;

  ngOnInit(): void {

    var Roles = JSON.stringify(this.globals.roles)

    if (!Roles.includes("ROLE_Bounty Administrator")) {
      this.router.navigate(["403"])
    }

    this.projectid = this.route.snapshot.params['srno'];

    this.http.delete(environment.apiURL + 'refreshJobs', { responseType: 'text' }).subscribe(response => {
      this.getProjectDetails();
    }, error => {
      if (error.error == "You are not Program Admin!") {
        this.router.navigate(['403']);
      }
    });

  }


  getProjectDetails() {
    this.http.get(environment.apiURL + `programs/` + this.projectid).subscribe(response => {
      this.project = response;

      if (this.project.status == 'DEACTIVE') {
        this.router.navigate(['403']);
      }
      this.http.get<any>(environment.apiURL + 'researchers').subscribe(response => {
        this.researcherList = response.data;

        for (var i = 0; i < this.project.invitedResearcherID.length; i++) {
          this.OriginalInvited.push(this.researcherList.find(o => o.emp_id === this.project.invitedResearcherID[i]));
        }

        var tempResearcherList = [];
        for (var i = 0; i < this.researcherList.length; i++) {

          var list = {
            srno: null,
            avatar: "",
            total_bounty_earned: 0,
            profile_pic:""
          }
          list.srno = this.researcherList[i].srno;
          list.avatar = this.researcherList[i].avatar;
          list.profile_pic = this.researcherList[i].profile_pic;
          if(! this.researcherList[i].total_bounty_earned){
            list.total_bounty_earned = null;
          }else{
            list.total_bounty_earned = this.researcherList[i].total_bounty_earned;
          }
         
          tempResearcherList.push(list);
        }

        this.allResearcherList =  tempResearcherList;
        this.project.invitedResearcher = this.OriginalInvited;
      });


      this.inScope = this.project.scope[0].inscope.split(':==:');
      this.outScope = this.project.scope[0].outscope.split(':==:');

      this.program_name = this.project.bprogramme_name;
      this.owner_empid = this.project.owner_empid;
      this.startdate = this.project.bprojectstart_date;
      this.startdate = new Date(this.startdate);

      this.enddate = this.project.bprojectend_date;
      this.enddate = new Date(this.enddate);

      this.api = this.project.bproject_api;
      this.technology = this.project.bproject_tech;
      this.description = this.project.bproject_description;
      this.criticality = this.project.bappln_criticality;
      this.unit = this.project.unit;
      this.staticpage = this.project.staticpage;
      this.dynamicpage = this.project.dynamicpage;
      this.roles = this.project.roles;
      this.overview = this.project.overview;
      this.access = this.project.access;
      this.resources = this.project.resources;
      this.jobs = this.project.jobdetialsdata;
      this.apm_id = this.project.apm_id;

      for (var i = 0; i < this.jobs.length; i++) {
        this.addedjobs.push(this.jobs[i].jobtype);
      }

      var category = Object.keys(this.project.rules);
      for (var i = 0; i < category.length; i++) {
        for (var j = 0; j < this.project.rules[category[i]].length; j++) {
          this.rules.push({ rules_category: this.project.rules[category[i]][j].rules_category, rules_details: this.project.rules[category[i]][j].rules_details })
        }
      }
      this.announcements = this.project.Announcement;
    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error.error });
      this.router.navigate(["/ProgramDetails"]);
    })
  }

  AddJob() {
    var data = { jobtype: this.newJob, pricing: this.newPricing, maxpricing: this.newMaxPricing, reward_type: this.reward_type };
    if (this.addedjobs.includes(this.newJob)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'You have already selected this Vulnerability Type' });
    } else if (this.newJob == null || this.newJob == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please select Vulnerability Type' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace_", this.newJob)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Vulnersbility type  0-9 a-z A-Z _ <space>' });
    } else if (this.newPricing < 1) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Reward min Value' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.newPricing)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'only numbers are allowed' });
    } else if (this.newPricing.toString().length > 8) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Reward min value can have 8 digits only' });
    } else if (this.newMaxPricing < 1) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Reward max Value' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.newMaxPricing)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'only numbers are allowed' });
    } else if (this.newMaxPricing.toString().length > 8) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Reward max value can have 8 digits only' });

    } else if (this.newMaxPricing < this.newPricing) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter valid Reward Value' });
    } else if (this.reward_type == null || this.reward_type == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please select reward type' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace_", this.reward_type)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in reward type  0-9 a-z A-Z _ <space>' });
    } else {
      this.jobs.push(data);
      this.newPricing = 0;
      this.newMaxPricing = 0;
      this.newJob = "";
      this.reward_type = "";
    }
  }

  Addrules() {
    var data = { rules_category: this.newCatg, rules_details: this.newrules };
    if (this.newCatg == null || this.newCatg.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please select Rule Category' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace_", this.newCatg)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed  for Rules category 0-9 a-z A-Z _ <space>' });
    } else if (this.newrules == null || this.newrules.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter valid Rule' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.newrules)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed Announcement a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@' });
    } else {
      this.rules.push(data);
      this.newrules = "";
      this.newCatg = "";
    }
  }

  AddAnnouncement() {

    var data = { announcement_category: this.newAnnoCat, announcement_details: this.newAnnouncement, createddate: new Date() };
    if (this.newAnnoCat == null || this.newAnnoCat.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please select Announcement Category' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace_", this.newAnnoCat)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed Announcement category 0-9 a-z A-Z _ <space>' });
    } else if (this.newAnnouncement == null || this.newAnnouncement.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Announcement' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.newAnnouncement)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed Announcement a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@' });
    } else {
      this.announcements.push(data);
      this.newAnnouncement = "";
      this.newAnnoCat = "";
    }
  }

  AddInScope() {
    if (this.newinScope == null || this.newinScope.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter valid InScope' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.newinScope)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed Inscope  a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@' });
    } else {
      this.inScope.push(this.newinScope)
      this.newinScope = "";
    }
  }

  deleteInScope(i) {
    this.inScope.splice(i, 1);
  }

  AddOutScope() {
    if (this.newoutscope == null || this.newoutscope.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter valid OutScope' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.newoutscope)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed out Scope a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@' });
    } else {
      this.outScope.push(this.newoutscope);
      this.newoutscope = "";
    }
  }

  deleteOutScope(i) {
    this.outScope.splice(i, 1);
  }

  deleteJob(i) {
    this.jobs.splice(i, 1);
    this.addedjobs.splice(i, 1);
  }

  deleteRules(i) {
    this.rules.splice(i, 1);
  }

  deleteAnnouncement(srno, i) {
    if (srno != null) {
      this.confirmationService.confirm({
        message: `Are you sure you want to delete ?`,
        accept: () => {
          this.http.delete(environment.apiURL + 'dAnnoucement/' + srno, { responseType: 'text' })
            .subscribe(
              data => {
                this.messageService.add({ severity: 'success', detail: data });
                this.announcements.splice(i, 1);
              },
              error => {
                if (error.error.ERROR_MSG == null) {
                  this.messageService.add({ severity: 'error', summary: 'Error', detail: "Some Error has occured!" });
                  return;
                }
                this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error.ERROR_MSG });
              });
        },
        reject: () => {
          this.messageService.add({ severity: 'warn', summary: 'Warn', detail: 'You have cancelled Delete Request' });
        }
      });

    } else {
      this.announcements.splice(i, 1);
    }


  }

  Submit() {

    if (!this.validateReportVulnDetails()) {
      return;
    }
    var researcherList = [];
    if (this.project.ptype == 'Private') {
      for (var i = 0; i < this.project.invitedResearcher.length; i++) {
        if (!this.OriginalInvited.includes(this.project.invitedResearcher[i]))
          researcherList.push(this.project.invitedResearcher[i].srno);
      }
    }

    var inscope = '', outscope = '';

    for (var i = 0; i < this.inScope.length; i++) {
      inscope = inscope + ':==:' + this.inScope[i];
    }

    for (var i = 0; i < this.outScope.length; i++) {
      outscope = outscope + ':==:' + this.outScope[i];
    }

    outscope = outscope?.substring(4, outscope.length);
    inscope = inscope?.substring(4, inscope.length);

    this.http.post(environment.apiURL + `updateprograms/` + this.projectid, {

      program_name: this.program_name,
      startdate: this.startdate,
      enddate: this.enddate,
      api: this.api,
      technology: this.technology,
      description: this.description,
      criticality: this.criticality,
      unit: this.unit,
      staticpage: this.staticpage,
      dynamicpage: this.dynamicpage,
      roles: this.roles,
      ptype: this.project.ptype,
      overview: this.overview,
      access: this.access,
      resources: this.resources,
      jobs: this.jobs,
      rules: this.rules,
      announcements: this.announcements,
      inScope: inscope,
      outScope: outscope,
      srno: this.projectid,
      researcherList: researcherList,
      apm_id: this.apm_id

    }, { responseType: 'text' }).subscribe(response => {
      this.messageService.add({ severity: 'success', detail: response });
      this.router.navigate(["/ProgramDetails"]);
    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error });
    });
  }

  checkboxClick(e) {
    if (this.project.invitedResearcher) {
      this.project.invitedResearcher = this.OriginalInvited;
    }
  }

  isExist(e) {
    
    if (this.OriginalInvited.find(o => o === e)) {
      return true;
    } else {
      return false;
    }
  }

  

  
  

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  


  validateReportVulnDetails() {
    var flaggeneralvalidate = false;

    if (this.program_name == null || this.program_name.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Programme Name' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace_", this.program_name)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Program Name 0-9 a-z A-Z _ <space>' });
    } else if (this.program_name.length > 100) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: ' Program Name can have maximum 100 characters' });
    }
    
    
    

    
    
    
    else if (this.apm_id.length > 0 && (this.apm_id.length > 10 || !this.tssvcommonvalidatorService.isRegexValid("alphanumeric", this.apm_id))) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Validations failed for APM ID' });
    } else if (this.description == null || this.description.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter  Brief' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.description)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Brief a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@)' });
    } else if (this.api < 1) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter no of api' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.api)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only numbers are allowed in  api' });
    } else if (this.dynamicpage < 1) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter no of dynamic page' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.dynamicpage)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only numbers are allowed in  Dynamic page field' });
    } else if (this.inScope.length == 0) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter InScope' });
    } else if (this.outScope.length == 0) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Outscope' });
    } else if (this.project.ptype == null || this.project.ptype.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please select Programme Type' });
    } else if (this.roles < 1) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter no of role' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.roles)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only numbers are allowed in  role field' });
    } else if (this.staticpage < 1) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter no of static page' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.staticpage)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only numbers are allowed in  Static Page field' });
    } else if (this.criticality == null || this.criticality.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please Select  Criticality' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace_", this.criticality)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Criticality 0-9 a-z A-Z _ <space>' });
    } else if (this.rules.length == 0) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Rules' });
    } else if (this.jobs.length < 4) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Rewards for all vulnerability Type' });
    } else if (this.announcements.length == 0) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Announcement' });
    } else if (this.technology == null || this.technology.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter valid Technology' });
    } else if (this.resources == null || this.resources.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Resources' });
    } else if (this.access == null || this.access.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Access' });
    } else if (this.overview == null || this.overview.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Overview' });
    }
    else if (this.enddate <= this.startdate) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'End date must be greater than start date' });
    }
    
    
    
    
    
    
    else {
      flaggeneralvalidate = true;
    }


    return flaggeneralvalidate;
  }

  getPrograms() {
    this.http.get(environment.apiURL + `programs/` + this.projectid).subscribe(response => {
      this.project = response;
    })
  }
}
