import { HttpClient } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Message, MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import * as XLSX from 'xlsx';
import { Globals } from '../globals';
@Component({
  selector: 'app-createnewprogramme',
  templateUrl: './createnewprogramme.component.html',
  styleUrls: ['./createnewprogramme.component.scss']
})
export class CreatenewprogrammeComponent implements OnInit {

  active4 = 0;
  empid: string = "";
  privateProgram = false;
  minDate: Date = new Date();
  preview: boolean = false;
  arrayBuffer;
  filelist: any[];
  file2;
  file3;
  file4;
  vunloptions;
  apm_id="";


  constructor(private router: Router,
    private messageService: MessageService,
    private globals: Globals,
    private http: HttpClient,
    private tssvcommonvalidatorService: TssvcommonvalidatorService) { }

  myDate = new Date();


  options = [
    { name: 'High', code: 'High' },
    { name: 'Critical', code: 'Critical' },
    { name: 'Medium', code: 'Medium' },
    { name: 'Low', code: 'Low' },
  ]


  invitedoptions = [
    { name: 'Public', code: 'Public' },
    { name: 'Private', code: 'Private' },
    { name: 'Protected', code: 'Protected' }
  ]

  rewardUnit = [
    { name: 'GEMS', code: 'GEMS' },
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
  newinScope = "";
  newoutscope = "";

  program_name: any;
  startdate: Date = new Date();
  enddate: Date = new Date();
  api: any;
  technology: any;
  description: any;
  criticality: any;
  returnMsg: any;
  staticpage: any;
  dynamicpage: any;
  roles: any;
  ptype: any;

  overview: any;
  access: any;
  resources: any;

  inScope: any[] = [];
  outScope: any[] = [];
  researcherList;
  programList;
  list2 = [];
  addedjobs = [];
  submit: boolean = true;
  Vpname: Message[];
  Verror: Message[];
  Vptype: Message[];
  Vcritical: Message[];
  Vbrief: Message[];
  Voverview: Message[];
  Vtech: Message[];
  Vapi: Message[];
  Vstatic: Message[];
  Vdynamic: Message[];
  Vroles: Message[];
  Vinscope: Message[];
  Voutscope: Message[];
  Vreward: Message[];
  VrewardError: Message[];
  Vrule: Message[];
  Vaccess: Message[];
  Vannounce: Message[];
  Vresources: Message[];
  Vapm_id: Message[];
  Vsdate: Message[];
  Vedate: Message[];
  Vcommon: Message[];
  uploadedFiles: any[];
  file;
  uploadedFile: File;
  categoryrules;
  Announcementption;
  ngOnInit(): void {

    this.http.get<any>(environment.apiURL + 'programs').subscribe(response => {
      this.programList = response.programs;
    });

    var Roles = JSON.stringify(this.globals.roles)

    if (!Roles.includes("ROLE_Bounty Administrator")) {
      this.router.navigate(["403"])
    }

    this.http.get<any>(environment.apiURL + 'researchers').subscribe(response => {
      this.researcherList = response.data;
    });

    this.http.post(environment.apiURL + 'getAllOptions', "P_Rules").subscribe(response => {
      this.categoryrules = response;

    });
    this.http.post(environment.apiURL + 'getAllOptions', "P_Announcement").subscribe(response => {
      this.Announcementption = response;

    });
    this.http.post(environment.apiURL + 'getAllOptions', "P_vunloptions").subscribe(response => {
      this.vunloptions = response;

    });
  }
  AddJob() {
    var data = { jobtype: this.newJob, pricing: this.newPricing, maxpricing: this.newMaxPricing, reward_type: this.reward_type };

    if (this.addedjobs.includes(this.newJob)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'You have already selected this Vulnerability Type' });
    } else if (this.newJob == null || this.newJob.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please select Vulnerability Type' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace_", this.newJob)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Vulnersbility type  0-9 a-z A-Z _ <space>' });
    } else if (this.newPricing < 1) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Reward min Value' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.newPricing)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'only numbers are allowed' });
    }else if (this.newPricing.toString().length > 8) {
      this.messageService.add({ severity: 'error', summary:'Error', detail: 'Reward min value can have 8 digits only' });
    } else if (this.newMaxPricing < 1) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Reward max Value' });
    }else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.newMaxPricing)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'only numbers are allowed' });
    }else if (this.newMaxPricing.toString().length > 8) {
      this.messageService.add({ severity: 'error', summary:'Error', detail: 'Reward max value can have 8 digits only' });
    } else if (this.newMaxPricing < this.newPricing) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter valid Reward Value' });
    } else if (this.reward_type == null || this.reward_type.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please select reward type' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace_", this.reward_type)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in reward type  0-9 a-z A-Z _ <space>' });
    } else {
      this.addedjobs.push(this.newJob);
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
    var data = { announcement_category: this.newAnnoCat, announcement_details: this.newAnnouncement };
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
    this.addedjobs.splice(this.jobs[i].jobtype)
    this.jobs.splice(i, 1);
  }

  deleteRules(i) {
    this.rules.splice(i, 1);
  }

  deleteAnnouncement(i) {
    this.announcements.splice(i, 1);
  }

  Submit() {
    if (!this.validateReportVulnDetails()) {
      return;
    }
    var researcherList = [];
    if (this.ptype == 'Private') {
      for (var i = 0; i < this.list2.length; i++) {
        researcherList.push(this.list2[i].srno);
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

    this.http.post(environment.apiURL + `programs`, {
      program_name: this.program_name,
      description: this.description,
      startdate: this.startdate,
      enddate: this.enddate,
      api: this.api,
      technology: this.technology,
      criticality: this.criticality,
      staticpage: this.staticpage,
      dynamicpage: this.dynamicpage,
      roles: this.roles,
      ptype: this.ptype,
      overview: this.overview,
      access: this.access,
      resources: this.resources,
      jobs: this.jobs,
      rules: this.rules,
      announcements: this.announcements,
      inScope: inscope,
      outScope: outscope,
      researcherList: researcherList,
      apm_id: this.apm_id

    }, { responseType: 'text' }).subscribe(response => {
      this.messageService.add({ severity: 'success', detail: response });
      this.router.navigate(["/ProgramDetails"]);
      this.Verror = [] ;
    }, error => {
      this.Verror = [
        { severity: 'warn', summary: 'Program Name', detail: error.error }
      ];
      
    });
  }

  typeChange() {
    if (this.ptype == 'Private') {
      this.privateProgram = true;
    } else {
      this.privateProgram = false;
    }
  }

  validateReportVulnDetails() {
    var flaggeneralvalidate = false;
    if (this.program_name == null || this.program_name.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Programme Name' });
    } 
    else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace_", this.program_name)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Program Name  0-9 a-z A-Z _ <space>' });
    } else if (this.program_name.length > 100) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'The length of program name should be less than 100 characters' });
    }
    else if (this.apm_id.length>0 && ( this.apm_id.length > 10 || !this.tssvcommonvalidatorService.isRegexValid("alphanumeric", this.apm_id))) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Validations failed for APM ID' });
    }

    else if (this.description == null || this.description.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter  Brief' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.description)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only following characters are allowed in Brief a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@)' });
    } else if (this.description.length > 10000) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Brief can have maximum 10000 characters'});
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
    } else if (this.ptype == null || this.ptype.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please select Programme Type' });
    } else if (this.roles < 1) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter no of role' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.roles)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Only numbers are allowed in  role field' });
    } else if (this.staticpage < 1) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter no of static page' });
    }else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.staticpage)) {
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


  validate() {
    
    var i = 0;

    if (this.program_name == null || this.program_name.trim() == '') {
      i = 0;
      this.Vpname = [
        { severity: 'warn', summary: 'Program Name', detail: 'Please enter Programme Name' }
      ];
    } else if (this.program_name.length < 3) {
      i = 0;
      this.Vpname = [
        { severity: 'error', summary: 'Program Name', detail: 'Programme Name must be greater than two characters' }
      ];
    } else if (!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace_", this.program_name)) {
      i = 0;
      this.Vpname = [{ severity: 'error', summary: 'Program Name', detail: 'Only following characters are allowed in Program Name 0-9 a-z A-Z _ <space>' }];
    }
    else {
      i = i + 1;
      this.Vpname = [
        { severity: 'success', summary: 'Program Name', detail: 'Validation Cleared!' }
      ];
    }


    if (this.ptype == null) {
      i = 0;
      this.Vptype = [
        { severity: 'warn', summary: 'Program Type', detail: 'Please Select a Programme Type' }
      ];
    }
    else {
      i = i + 1;
      this.Vptype = null
    }
    if (this.criticality == null) {
      i = 0;
      this.Vcritical = [
        { severity: 'warn', summary: 'Program Criticality', detail: 'Please Select a Programme Criticality' }
      ];
    }
    else {
      i = i + 1;
      this.Vcritical = null
    }
    if (this.description == null || this.description.trim() == ' ') {
      i = 0;
      this.Vbrief = [{ severity: 'warn', summary: 'Program Brief', detail: 'Please enter  Brief' }];
    } else if (!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.description)) {
      i = 0;
      this.Vbrief = [{ severity: 'error', summary: 'Program Brief', detail: 'Only following characters are allowed in Brief a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@)' }];
    } else if (this.description.length < 5) {
      i = 0;
      this.Vbrief = [{ severity: 'error', summary: 'Program Brief', detail: 'Please enter a valid Brief with more than 10 characters ' }];
    } else {
      i = i + 1;
      this.Vbrief = [{ severity: 'success', summary: 'Program Brief', detail: 'Validation Cleared!' }];
    }
    if (this.overview == null || this.overview == ' ') {
      i = 0;
      this.Voverview = [{ severity: 'warn', summary: 'Functional Description', detail: 'Please enter Description' }];
    } else if (this.overview.length < 12) {
      i = 0;
      this.Voverview = [{ severity: 'error', summary: 'Functional Description', detail: 'Please enter a valid Description with more than 10 characters ' }];
    } else {
      i = i + 1;
      this.Voverview = [{ severity: 'success', summary: 'Functional Description', detail: 'Validation Cleared!' }];
    }
    if (this.technology == null || this.technology == ' ') {
      i = 0;
      this.Vtech = [{ severity: 'warn', summary: 'Technology Used', detail: 'Please enter Technology Used' }];
    } else if (this.technology.length < 12) {
      i = 0;
      this.Vtech = [{ severity: 'error', summary: 'Technology Used', detail: 'Please enter a valid Technology Used with more than 10 characters ' }];
    } else {
      i = i + 1;
      this.Vtech = [{ severity: 'success', summary: 'Technology Used', detail: 'Validation Cleared!' }];
    }
    if (this.api == null) {
      i = 0;
      this.Vapi = [{ severity: 'warn', summary: 'No. of API', detail: 'Please enter no of api' }];
    } else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.api)) {
      i = 0;
      this.Vapi = [{ severity: 'error', summary: 'No. of API', detail: 'Only numbers are allowed in  api' }];
    } else { 
      i++
      this.Vapi = null;
    }
    if (this.staticpage == null) {
      i = 0;
      this.Vstatic = [{ severity: 'warn', summary: 'No. of Static Page', detail: 'Please enter no of Static Page' }];
    } else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.staticpage)) {
      i = 0;
      this.Vstatic = [{ severity: 'error', summary: 'No. of Static Page', detail: 'Only numbers are allowed in  Static Page' }];
    }else {
      i++
      this.Vstatic = null;
    }
    if (this.dynamicpage == null) {
      i = 0;
      this.Vdynamic = [{ severity: 'warn', summary: 'No. of Dynamic Page', detail: 'Please enter no of Dynamic Page' }];
    }else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.api)) {
      i = 0;
      this.Vdynamic = [{ severity: 'error', summary: 'No. of Dynamic Page', detail: 'Only numbers are allowed in  Dynamic Page' }];
    } else {
      i++
      this.Vdynamic = null;
    }
    if (this.roles == null) {
      i = 0;
      this.Vroles = [{ severity: 'warn', summary: 'No. of Roles', detail: 'Please enter no of Roles' }];
    } else if (!this.tssvcommonvalidatorService.isRegexValid("numeric", this.roles)) {
      i = 0;
      this.Vroles = [{ severity: 'error', summary: 'No. of Roles', detail: 'Only numbers are allowed in Roles' }];
    } else {
      i++
      this.Vroles = null;
    }
    if (this.inScope.length == 0) {
      i = 0;
      this.Vinscope = [{ severity: 'error', summary: 'In Scope', detail: 'Please enter In Scope' }];
    } else {
      i=i+1;
      this.Vinscope = [{ severity: 'success', summary: 'In Scope', detail: 'Validation Cleared!' }];
    }
    if (this.outScope.length == 0) {
      i = 0;
      this.Voutscope = [{ severity: 'error', summary: 'Out Scope', detail: 'Please enter Out Scope' }];
    }
    else {
      i=i+1;
      this.Voutscope = [{ severity: 'success', summary: 'Out Scope', detail: 'Validation Cleared!' }];
    }
    if (this.jobs.length < 4 ) {
      i = 0;
      
      this.Vreward = [{ severity: 'error', summary: 'Rewards', detail: 'Please enter Rewards for all vulnerability Type' }];
    } else if (this.jobs.length == 0) {
      i = 0;
      this.Vreward = [{ severity: 'warn', summary: 'Rewards', detail: 'Please enter Rewards' }];
    } else {
      i=i+1;
      this.Vreward = [{ severity: 'success', summary: 'Rewards', detail: 'Validation Cleared!' }];
    }
    if (this.rules.length == 0) {
      i = 0;
      this.Vrule = [{ severity: 'warn', summary: 'Rules', detail: 'Please enter Rules' }];
    } else {
      i=i+1;
      this.Vrule = [{ severity: 'success', summary: 'Rules', detail: 'Validation Cleared!' }];
    }
    if (this.access == null) {
      i = 0;
      this.Vaccess = [{ severity: 'warn', summary: 'Access', detail: 'Please enter Access' }];
    } else if (this.access.length < 12) {
      i = 0;
      this.Vaccess = [{ severity: 'error', summary: 'Access', detail: 'Please enter a valid Access with more than 20 characters' }];
    } else {
      i = i + 1;
      this.Vaccess = [{ severity: 'success', summary: 'Access', detail: 'Validation Cleared!' }];
    }
    if (this.announcements.length == 0) {
      i = 0;
      this.Vannounce = [{ severity: 'warn', summary: 'Announcements', detail: 'Please enter Announcements' }];
    } else {
      i=i+1;
      this.Vannounce = [{ severity: 'success', summary: 'Announcements', detail: 'Validation Cleared!' }];
    }
    if (this.resources == null) {
      i = 0;
      this.Vresources = [{ severity: 'warn', summary: 'Resources', detail: 'Please enter Resources' }];
    } else if (this.resources.length < 12) {
      i = 0;
      this.Vresources = [{ severity: 'error', summary: 'Resources', detail: 'Please enter valid Resources with more than 20 characters' }];
    } else {
      i = i + 1;
      this.Vresources = [{ severity: 'success', summary: 'Resources', detail: 'Validation Cleared!' }];
    }

    if (this.enddate <= this.startdate) {
      i = 0
      this.Vsdate = [
        { severity: 'error', summary: 'Date', detail: 'End date must be greater than start date' }
      ];
    }
    else {
      i = i + 1
      this.Vsdate = [{ severity: 'success', summary: 'Date', detail: 'Validation Cleared!' }];
    }

    if (i == 18) {
      this.submit = false;
    } else {
      this.submit = true;
    }

    for(let i=0;i<this.programList.length;i++){
      if(this.program_name == this.programList[i].bprogramme_name){
        this.Vpname = [{ severity: 'error', summary: 'Program Name', detail: 'Programme with same name already exists' }]
        return ;
      }
    }
  }
  onSelect(evt: any) {
    this.uploadedFile = evt[0];
    let fileReader = new FileReader();
    fileReader.readAsArrayBuffer(this.uploadedFile);
    fileReader.onload = (e) => {
      this.arrayBuffer = fileReader.result;
      var data = new Uint8Array(this.arrayBuffer);
      var arr = new Array();
      for (var i = 0; i != data.length; ++i) arr[i] = String.fromCharCode(data[i]);
      var bstr = arr.join("");
      var workbook = XLSX.read(bstr, { type: "binary" });
      var first_sheet_name = workbook.SheetNames[0];
      var worksheet = workbook.Sheets[first_sheet_name];
      var arraylist = XLSX.utils.sheet_to_json(worksheet, { raw: true });

      var sheet_two = workbook.SheetNames[1];
      var worksheet2 = workbook.Sheets[sheet_two];
      var arraylist2 = XLSX.utils.sheet_to_json(worksheet2, { raw: true });

      this.filelist = [];
      this.file = arraylist;
      this.file2 = arraylist2;

      for (var i = 0; i < this.file.length; i++) {
        if (i == 0) {
          this.program_name = this.file[i].Value;
        }
        if (this.file[i].Field == "Program Type") {
          this.ptype = this.file[i].Value;
          if (this.ptype == 'Private') {
            this.privateProgram = true;
          } else {
            this.privateProgram = false;
          }
        }
        if (this.file[i].Field == "APM ID") {
          this.apm_id = this.file[i].Value;
        }
        if (this.file[i].Field == "Program Criticality") {
          this.criticality = this.file[i].Value;
        }
        if (this.file[i].Field == "Brief") {
          this.description = this.file[i].Value;
        }
        if (this.file[i].Field == "Functionality Description") {
          this.overview = this.file[i].Value;
        }
        if (this.file[i].Field == "Technology Used") {
          this.technology = this.file[i].Value;
        }
        if (this.file[i].Field == "Static Page") {
          this.staticpage = this.file[i].Value
        }
        if (this.file[i].Field == "Dynamic Page") {
          this.dynamicpage = this.file[i].Value;
        }
        if (this.file[i].Field == "API") {
          this.api = this.file[i].Value;
        }
        if (this.file[i].Field == "Roles") {
          this.roles = this.file[i].Value;
        }
        if (this.file[i].Field == "Inscope") {
          this.inScope = this.file[i].Value.split(":==:");
        }
        if (this.file[i].Field == "Outscope") {
          this.outScope = this.file[i].Value.split(":==:");
        }

        if (this.file[i].Field == "Access Information") {
          this.access = this.file[i].Value;
        }
        if (this.file[i].Field == "Resources") {
          this.resources = this.file[i].Value;
        }

        if (this.file[i].Field == "Start Date") {
          this.startdate = new Date(Date.parse(this.file[i].Value));
        }

        if (this.file[i].Field == "End Date") {
          this.enddate = new Date(Date.parse(this.file[i].Value));
        }

      }

      for (var i = 0; i < this.file2.length; i++) {
        this.newJob = this.file2[i].Type
        this.newPricing = this.file2[i].Min
        this.newMaxPricing = this.file2[i].Max
        this.reward_type = this.file2[i].Award
        this.AddJob()
      }

      var sheet_three = workbook.SheetNames[2];
      var worksheet3 = workbook.Sheets[sheet_three];
      var arraylist3 = XLSX.utils.sheet_to_json(worksheet3, { raw: true });
      this.file3 = arraylist3
      for (var i = 0; i < this.file3.length; i++) {
        this.newCatg = this.file3[i].Category
        this.newrules = this.file3[i].Rule
        this.Addrules()
      }
      var sheet4 = workbook.SheetNames[3];
      var worksheet4 = workbook.Sheets[sheet4];
      var arraylist4 = XLSX.utils.sheet_to_json(worksheet4, { raw: true });
      this.file4 = arraylist4
      for (var i = 0; i < this.file4.length; i++) {
        this.newAnnoCat = this.file4[i].Category
        this.newAnnouncement = this.file4[i].Data
        this.AddAnnouncement()
      }

    }

  }
  download() {
    window.open('assets/template/ProgramData.xlsx')
  }
}


