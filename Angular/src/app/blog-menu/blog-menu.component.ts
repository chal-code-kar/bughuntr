import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';
import { Globals } from '../globals';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';


@Component({
  selector: 'app-blog-menu',
  templateUrl: './blog-menu.component.html',
  styleUrls: ['./blog-menu.component.scss']
})
export class BlogMenuComponent implements OnInit {
  blogs: any;
  selected;
  AddDialog: boolean = false;
  EditDialog: boolean = false;
  deleteDialog: boolean = false;
  data;
  title;
  brief;
  tags = [];
  blogTemp;
  tagged;
  category: any;
  newcat: any;
  addcat: boolean = false;
  blogObj:any;

  constructor(private http: HttpClient,
    private router: Router,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,
    private messageService: MessageService,
    private globals: Globals) { }

  ngOnInit(): void {


    this.tssvcommonvalidatorService.isAccessible();
    this.getData()
  }
  getData() {
    this.http.get<any>(environment.apiURL + 'blogs').subscribe(response => {
      this.blogs = response.content
      this.blogTemp = this.blogs
      for (var i = 0; i < this.blogTemp.length; i++) {
        const myArray = this.blogTemp[i].category.split(",");
        if (myArray.length == 1) {
          this.tags.push(myArray[0])
        } else {
          for (var j = 0; j < myArray.length; j++) {
            this.tags.push(myArray[j])
          }
        }
      }
      var unique = this.tags.filter((v, i, a) => a.indexOf(v) === i);
      this.tags = unique

      this.blogs.sort((a, b) => b.createdate - a.createdate)
    });
  }
  AddCat() {
    var tag = this.newcat 
    if (tag.length > 100) {
      this.messageService.add({ severity: 'error', detail: "The length of Category should be less than 100 characters" });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("name", tag)) {
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in Category .a-zA-Z0-9_()&,-' });
    } else {
      this.tags.push(tag)
    }
    this.addcat = false;
    this.newcat = null;
  }
  openAdd() {

    this.AddDialog = true;
    this.category = null;
    this.data.title = "";
    this.data.brief = "";
    

  }
  openEdit(e) {
    this.EditDialog = true;
    for (var i = 0; i < this.blogs.length; i++) {
      if (e == this.blogs[i].srno) {
        this.data = this.blogs[i]
        this.category = this.blogs[i].category.split(",");
      }
    }
  }
  openDelete(e) {
    this.deleteDialog = true;
    for (var i = 0; i < this.blogs.length; i++) {
      if (e == this.blogs[i].srno) {
        this.data = this.blogs[i]
      }
    }
  }
  update() {
    this.title = this.data.title
    this.brief = this.data.brief

    if (this.category.length == 1) {
      this.tagged = this.category[0]
    } else {
      this.tagged = this.category[0]
      for (var i = 1; i < this.category.length; i++) {
        this.tagged = this.tagged.concat(",", this.category[i])
      }
    }

    if(this.validateBlog()){
            
  
      var data =
      {
        title: this.data.title,
        brief: this.data.brief,
        srno: this.data.srno,
        category: this.tagged
      }
      this.http.post(environment.apiURL + 'updateBlog', data, { responseType: 'text' }).subscribe(response => {
        this.hideDialog()
        this.getData();
        this.messageService.add({ severity: 'success', detail: response });
      }, error => {
        this.messageService.add({ severity: 'error', detail: error.error });
      });
    }
  }
  delete(e) {
    this.http.delete(environment.apiURL + 'deleteBlog/' + e, { responseType: 'text' }).subscribe(response => {
      this.getData()
      this.deleteDialog = false;
      this.messageService.add({ severity: 'success', detail: response });
    }, error => {
      this.deleteDialog = false;
      this.messageService.add({ severity: 'error', detail: error.error });
    });
  }
  add() {
    if (this.validateBlog()) {

      if (this.tagged.length == 1) {
        this.category = this.tagged[0]
      }
      else {
        this.category = this.tagged[0];
        for (var i = 1; i < this.tagged.length; i++) {
          this.category = this.category.concat(",", this.tagged[i])
        }
      }
      var data =
      {
        title: this.title,
        brief: this.brief,
        category: this.category
      }

      this.http.post(environment.apiURL + 'addBlog', data , { responseType: 'text' }).subscribe(response => {
        this.getData()
        this.messageService.add({ severity: 'success', detail: "Added blog successfully!" });
        this.hideDialog()

      }, error => {
        this.messageService.add({ severity: 'error', detail: error.error });
      });
    }
    
  }
  hideDialog() {
    this.title="";
    this.brief="";
    this.EditDialog = false;
    this.deleteDialog = false;
    this.AddDialog = false;
    this.addcat = false;
    this.newcat = null;
    this.data = null;
    this.tagged = null;
    this.category = null;

   
  }

  validateBlog() {
    
    if (!this.title || this.title == null || this.title.length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please enter Title' });
    } else if (!this.tssvcommonvalidatorService.isRegexValid("name", this.title)) {
      this.messageService.add({ severity: 'error', detail: "Only following characters are allowed in title .a-zA-Z0-9_()&,-" });
    } else if (this.title.length > 60) {
      this.messageService.add({ severity: 'error', detail: 'The length of Title should be less than 60 characters' });
    }

    else if (!this.tagged || this.tagged == undefined || this.tagged == null || this.tagged.length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please Select Category' });
    }

    else if (!this.brief || this.brief == null || this.brief.length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please enter Brief' });
    } else {
      return true;
    }
    return false;
  }


}
