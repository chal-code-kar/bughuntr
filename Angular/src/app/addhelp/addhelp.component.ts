import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem, MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';
import * as Quill from 'quill';

import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import { Globals } from '../globals';

@Component({
  selector: 'app-addhelp',
  templateUrl: './addhelp.component.html',
  styleUrls: ['./addhelp.component.scss']
})
export class AddhelpComponent implements OnInit {

  parent_srno = 0;
  help: any;
  helpPages: any;
  path = "";
  SelectedSection: any;
  catg: any[] = [];
  items: MenuItem[];

  home: MenuItem;
  helpDetails: any;
  model: any[];

  constructor(private router: Router,
    private http: HttpClient,
    private messageService: MessageService,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,
    private globals:Globals) { }

  name: any;
  description: any;
  parentName: any;
  parent_menu: any;
  sub_parent_menu: any;
  sub_parent_child_menu: any;


  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)

    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }

    this.tssvcommonvalidatorService.isAccessible();

    this.http.get<any>(environment.apiURL + 'help').subscribe(response => {
      this.help = response.content;
      if(response.isAdmin == false){
        this.messageService.add({ severity: 'error', summary:'Error', detail: 'You don\'t have Admin access.' });
        this.router.navigate(["/"]);
      }
      this.helpDetails = response.content;
      var key = "srno";
      this.helpDetails = [...new Map(this.helpDetails.map(item => [item[key], item])).values()];
      var arr = [];
      for (var i = 0; i < this.helpDetails.length; i++) {
        var item = { id: Number, label: '', order_no: Number, parentid: Number, description: '' };
        item.id = this.helpDetails[i].srno;
        item.label = this.helpDetails[i].name;
        item.order_no = this.helpDetails[i].order_no;
        item.parentid = this.helpDetails[i].parentsrno;
        item.description = this.helpDetails[i].description;
        arr.push(item);
      }
      this.model = this.list_to_tree(arr);
      this.model = this.removeItems(this.model);
    });
  }

  list_to_tree(list) {
    var map = {}, node, roots = [], i;
    for (i = 0; i < list.length; i += 1) {
      map[list[i].id] = i; 
      list[i].items = []; 
    }
    for (i = 0; i < list.length; i += 1) {
      node = list[i];
      if (node.parentid != 0) {
        if (node.parentid != -1) {
          list[map[node.parentid]].items.push(node);
        }
      } else {
        roots.push(node);
      }
    }
    return roots;
  }

  removeItems(array) {
    for (var i = 0; i < array.length; i++) {
      if (array[i].items.length == 0) {
        delete array[i].items;
      } else {
        this.removeItems(array[i].items);
      }
    }
    return array;
  }

  submit() {
    var parent_srno;
    if (this.parent_menu == undefined) {
      parent_srno = 0
    } else if (this.sub_parent_menu == undefined) {
      parent_srno = this.parent_menu.id
    } else if (this.sub_parent_child_menu == undefined) {
      parent_srno = this.sub_parent_menu.id
    } else if (this.sub_parent_child_menu) {
      parent_srno = this.sub_parent_child_menu.id
    }

    if(this.validationforHelp()){
      this.http.post(environment.apiURL + `help`, {
        name: this.name,
        description: this.description,
        parent_srno: parent_srno
      }, { responseType: 'text' }).subscribe(response => {
        this.messageService.add({ severity: 'success', detail: response });
        this.router.navigate(["/Help"]);
      },error => {
        this.messageService.add({ severity: 'error', detail: error.error });
      });
    }
  }

  validationforHelp() { 
    var flaggeneralvalidate = false;
    if (this.name == null || this.name.length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please enter Menu Name' });
    } else if(!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace", this.name)){
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in Menu Name 0-9 a-z A-Z <space>' });
    } else if(this.name.length > 100){
      this.messageService.add({ severity: 'error', detail: 'The length of Menu Name should be less than 100 character' });
    } else if (this.description == null || this.description.length < 4) {
      this.messageService.add({ severity: 'error', detail: 'Please enter valid Description' });
    } else {
      flaggeneralvalidate = true;
    }
    return flaggeneralvalidate;
  }
}
