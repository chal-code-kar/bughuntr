import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';
import { Globals } from '../globals';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';

@Component({
  selector: 'app-menu-table',
  templateUrl: './menu-table.component.html',
  styleUrls: ['./menu-table.component.scss'],
})
export class MenuTableComponent implements OnInit {
  menus;
  selected;
  roles;
  AddDialog: boolean = false;
  EditDialog: boolean = false;
  deleteDialog: boolean = false;
  data;
  name;
  link;
  role = [];
  tagged;
  menu;

  checked: boolean = true;

  constructor(
    private router: Router,
    private http: HttpClient,
    private messageService: MessageService,
    private globals: Globals,
    private tssvcommonvalidatorService: TssvcommonvalidatorService
  ) {}

  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)
   
    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }

    this.tssvcommonvalidatorService.isAccessible();
    this.getData();
  }
  getData() {
    this.http.get<any>(environment.apiURL + 'menus').subscribe(
      (response) => {
        this.menus = response.content;
        var arr = [];
        
        this.http.get<any>(environment.apiURL + 'rolesAndPermission').subscribe(
          (response) => {
            this.roles = response.totalRoles;
            for (var i = 0; i < this.menus.length; i++) {
              
              
              var item = {
                srno: Number,
                menuname: '',
                link: '',
                roles:''
              };
              item.srno = this.menus[i].srno;
              item.menuname = this.menus[i].menuname;
              item.link = this.menus[i].link;
              item.roles=" ";
              var role = this.menus[i].role.split(',');
              
              
              for (var j = 0; j < role.length; j++) {
                
                for (var k = 0; k < this.roles.length; k++) {
                  if (parseInt(role[j]) == this.roles[k].srno) {
                    if (this.roles[k].role_name == 'Administrator') {
                        item.roles=item.roles+","+"Administartor";
                    }
                    if (this.roles[k].role_name == 'Bounty Administrator') {
                      item.roles=item.roles+","+"Bounty Administartor";
                     }
                    if (this.roles[k].role_name == 'Researcher') {
                      item.roles=item.roles+","+"Researcher";
                    }
                    if (this.roles[k].role_name == 'Visitor') {
                      item.roles=item.roles+","+"Visitor";
                    }
                  }
                }

              }
              item.roles = item.roles.slice(2, item.roles.length)
              arr.push(item);
            }
            this.menu=arr;
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: error.error,
            });
          }
        );
      },
      (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: error.error,
        });
      }
    );
  }
  delete(e) {
    this.deleteDialog = true;
    for (var i = 0; i < this.menus.length; i++) {
      if (e === this.menus[i].srno) {
        this.data = this.menus[i];
      }
    }
  }
  edit(e) {
    this.EditDialog = true;
    for (var i = 0; i < this.menus.length; i++) {
      if (e === this.menus[i].srno) {
        this.data = this.menus[i];
        var temp = this.data.role.split(',');
        for (var j = 0; j < temp.length; j++) {
          this.role.push(parseInt(temp[j]));
        }
        this.tagged = this.role;
      }
    }
  }
  update() {
    this.selected = ' ';
    for (var i = 0; i < this.tagged.length; i++) {
      this.selected = this.selected + ',' + this.tagged[i];
    }
    this.selected = this.selected.slice(2, this.selected.length);
    if(this.validate()){
      var data = {
        menuname: this.data.menuname,
        link: this.data.link,
        role: this.selected,
        srno: this.data.srno,
      };
      this.http
        .post(environment.apiURL + 'updateMenu', data, { responseType: 'text' })
        .subscribe(
          (response) => {
            this.hide();
            this.ngOnInit();
            this.messageService.add({ severity: 'success', detail: response });
            this.router.navigate(['/menus']);
          },
          (error) => {
            this.messageService.add({ severity: 'error', detail: error.error });
            this.router.navigate(['/']);
          }
        );
    }

  }

  validate() {
    var flaggeneralvalidate = false;

    if (this.data.menuname == null || this.data.menuname.trim() == '') {
      this.messageService.add({ severity: 'error', detail: 'Please enter Menu Name' });
    }else if (!this.tssvcommonvalidatorService.isRegexValid("description", this.data.menuname)) {
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in Menu Name \n.a-zA-Z0-9_()&,-:/ <space>' });
    } else if (this.data.menuname.length >100) {
      this.messageService.add({ severity: 'error', detail: 'The length of Menu Name should be less than 100 characters' });
    } 

    else if (this.data.link == null || this.data.link.trim() == '') {
      this.messageService.add({ severity: 'error', detail: 'Please enter Link' });
    }else if (!this.tssvcommonvalidatorService.isRegexValid("description", this.data.link)) {
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in Link \n.a-zA-Z0-9_()&,-:/ <space>' });
    } else if (this.data.link.length >100) {
      this.messageService.add({ severity: 'error', detail: 'The length of Link should be less than 100 characters' });
    } 

    else if (!this.tagged || this.tagged == undefined || this.tagged == null || this.tagged.length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please Select Role' });
    }else {
      flaggeneralvalidate = true;
    }
    return flaggeneralvalidate;
  }

  hide() {
    this.EditDialog = false;
    this.deleteDialog = false;
    this.data = null;
    this.role = [];
    this.tagged = null;
    this.getData();
  }

  remove() {
    this.http
      .delete(environment.apiURL + 'deleteMenu/' + this.data.srno, {
        responseType: 'text',
      })
      .subscribe(
        (response) => {
          this.hide();
          this.ngOnInit();
          this.messageService.add({ severity: 'success', detail: response });
        },
        (error) => {
          this.hide();
          this.messageService.add({ severity: 'error', detail: error.error });
        }
      );
  }
}
