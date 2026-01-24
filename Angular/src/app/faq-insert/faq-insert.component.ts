import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Router, ActivatedRoute, } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import { Globals } from '../globals';

@Component({
  selector: 'app-faq-insert',
  templateUrl: './faq-insert.component.html',
  styleUrls: ['./faq-insert.component.scss']
})
export class FaqInsertComponent implements OnInit {
  FAQdialog: boolean;
  srno: any;


  constructor(private http: HttpClient,
    private router: Router,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private route: ActivatedRoute,
    private globals:Globals,
    private tssvcommonvalidatorService: TssvcommonvalidatorService) {
    this.route.params.subscribe(params => this.srno = params.srno);

  }
  faqtitle;
  description;
  data;
  querydialog: any;
  Faqinsert;
  showfaq;
  data1;

  ngOnInit(): void {


    this.tssvcommonvalidatorService.isAccessible();
    this.Faqshow();
  }

  FaqInsert() {
    var Faqinsert =
    {
      faqtitle: this.faqtitle,
      description: this.description
    }
    if(this.validationPostfaq()) {

    this.http.post(environment.apiURL + 'FaqPostQuery', Faqinsert, { responseType: 'text' }).subscribe(response => {

      this.data = response;
      this.Faqshow();

      
      this.querydialog = false;
      this.faqtitle = null;
      this.description = null;
      this.messageService.add({ severity: 'success', summary: 'Success', detail: "FAQ Added Successfully" });

    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error });
      this.querydialog = false;
    });
  }
  }

  Faqshow() {
    this.http.get<any>(environment.apiURL + 'FaqAllQuery').subscribe(response => {
      this.showfaq = response;
      
    })
  }

  QueryPost() {
    this.querydialog = true;
    this.faqtitle = null;
    this.description = null;
  }
  hide() {
    this.querydialog = false;
    this.faqtitle = null;
    this.description = null;

  }
  deleteFAQ(srno) {


    this.confirmationService.confirm({

      message: `Are you sure you want to delete ?`,

      accept: () => {

        this.http.get(environment.apiURL + 'deleteFAQ/' + srno, { responseType: 'text' })
          .subscribe(
            data => {
              this.messageService.add({ severity: 'success', summary: 'Success', detail: "Deleted Succcessfully!" });
              this.Faqshow();
            },
            error => {
              if (error.error.ERROR_MSG == null) {
                this.messageService.add({ severity: 'error', summary: 'Error', detail: "An error has occurred, please try again or contact the administrator" });
                return;
              }
              
            });
      },
      reject: () => {
        this.messageService.add({ severity: 'warn', summary: 'Warn', detail: 'You have cancelled Delete Request' });
      }
    });

  }
  updateFAQ() {

    var data1 =
    {
      faqtitle: this.faqtitle,
      description: this.description
    }
    
    if(this.validationPostfaq()) {
    this.http.post(environment.apiURL + 'updateFAQ/' + this.srno, data1, { responseType: 'text' }).subscribe(response => {
      this.Faqshow();
      this.hideFAQ();
      this.messageService.add({ severity: 'success', detail: response });
    }, error => {
      this.messageService.add({ severity: 'error', detail: error.error });
    });
  }
  }
  QueryFAQ(srno) {
    this.FAQdialog = true;
    for (var i = 0; i < this.showfaq.length; i++) {
      if (srno == this.showfaq[i].srno) {
        this.faqtitle = this.showfaq[i].faqtitle
        this.description = this.showfaq[i].description
        this.srno = srno;
      }
    }
  }
  hideFAQ() {
    this.FAQdialog = false;
  }

  validationPostfaq() {

    var flaggeneralvalidate = false;
    
    if (this.faqtitle == null || this.faqtitle.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Question' });
    }else if (!this.tssvcommonvalidatorService.isRegexValid("newquestion", this.faqtitle)) {
      this.messageService.add({ severity: 'error', detail: "Only following characters are allowed in Questions Field  0-9 a-z A-Z <space> . _ ( ) & - ?" });
    } else if (this.faqtitle.length >500) {
      this.messageService.add({ severity: 'error', detail: 'The length of Question should be less than 500 characters' });
    } else if (this.description == null || this.description.trim() == '') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter Description' });
    }
    else {
      flaggeneralvalidate = true;
    }

    return flaggeneralvalidate;
  }
}

