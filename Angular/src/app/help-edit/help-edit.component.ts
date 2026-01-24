import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';
import { Router } from '@angular/router';
import { Globals } from '../globals';

@Component({
  selector: 'app-help-edit',
  templateUrl: './help-edit.component.html',
  styleUrls: ['./help-edit.component.scss']
})
export class HelpEditComponent implements OnInit {
  help: any[];
  helpDetails:any;
  model: any;
  selected;
  disable: boolean;
  data:any;
  helpDialog: boolean;
  deleteDialog: boolean;
  parent_menu: any;
  sub_parent_menu: any;
  sub_parent_child_menu: any;
  url :any;
  parent:any;
  sub_parent:any;
  sub_parent_child:any;
  items: any;
  sub_parent_main:any;
  sub_parent_child_main:any;
  constructor(private http: HttpClient,
    private messageService: MessageService,
    private router: Router,
    private globals:Globals,
    private tssvcommonvalidatorService: TssvcommonvalidatorService,
    ) { }

  ngOnInit(): void {

    var Roles=JSON.stringify(this.globals.roles)

    if(!Roles.includes("ROLE_Administrator"))
    {
      this.router.navigate(["403"])
    }


    this.tssvcommonvalidatorService.isAccessible();
    this.getData();
  }
  getData(){

    var help=[];
    this.http.get<any>(environment.apiURL + 'help').subscribe(response => {
      this.helpDetails = response.content;
      for( var i=0;i<this.helpDetails.length;i++){
        if(this.helpDetails[i].description!=null && this.helpDetails[i].parentsrno!=0){
          help.push(this.helpDetails[i])
        }
      }
      this.help=help
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
  deletehelp(e) {
      this.http.get(environment.apiURL + 'deletehelp/'+e, { responseType: 'text' }).subscribe(response => {
          this.getData();
          this.deleteDialog=false;
          this.messageService.add({severity:'success', detail: response.toString()});
  },error => {
    this.messageService.add({ severity: 'error', detail: error.error });
  });
  }
  openDelete(e)
  {
    this.deleteDialog=true;
    for(var i=0;i<this.help.length;i++){
      if(this.help[i].srno==e){
        this.data=this.help[i]
      }
    }

  }
editHelp(e) {
  this.helpDialog = true;
  this.disable=true;
  for(var i=0;i<this.help.length;i++){
    if(this.help[i].srno==e){
      this.data=this.help[i]

      if(this.data.parentsrno==-1){
        this.parent=null
      }
      else{
        this.pathManager(this.data.parentsrno)
        this.options()
      }
    }
  }
  
}
hideDialog() {
  this.helpDialog = false;
  this.deleteDialog=false;
  this.data=null;
  this.parent=null
  this.parent_menu=null
  this.sub_parent=null;
  this.sub_parent_child=null;
  this.sub_parent_menu=null;
  this.sub_parent_child_menu=null;
  this.sub_parent_main=null;
  this.sub_parent_child_main=null;
  this.getData()
}

options(){
  var sub_parent_main=[];
  var sub_parent_child_main=[];
  if(this.sub_parent_child!=null ){
    if(this.sub_parent!=null){
      for(var i=0;i<this.helpDetails.length;i++){
          if(this.sub_parent.parentsrno==this.helpDetails[i].parentsrno){
            sub_parent_main.push(this.helpDetails[i].name)
          }
      }
      this.sub_parent_main=sub_parent_main;
      for(var i=0;i<this.helpDetails.length;i++){
        if(this.sub_parent_child.parentsrno==this.helpDetails[i].parentsrno){
          sub_parent_child_main.push(this.helpDetails[i].name)
        }
      }
      this.sub_parent_child_main=sub_parent_child_main;
    }
    else{
      for(var i=0;i<this.helpDetails.length;i++){
        if(this.sub_parent_child.parentsrno==this.helpDetails[i].parentsrno){
          sub_parent_child_main.push(this.helpDetails[i].name)
        }
      }
      this.sub_parent_child_main=sub_parent_child_main;
    }
  }
}

clearMenu(){
 this.disable=false;
 this.parent=null;
 this.sub_parent=null;
 this.sub_parent_child=null;
}

saveHelp(e) {
  
  var parent_srno;
    if (this.parent_menu == undefined) {
      if(this.data.parentsrno==-1){
        parent_srno=-1
      }
      else{
        parent_srno = this.data.parentsrno
      }
    } else if (this.sub_parent_menu == undefined) {
      parent_srno = this.parent_menu.id
    } else if (this.sub_parent_child_menu == undefined) {
      parent_srno = this.sub_parent_menu.id
    } else if (this.sub_parent_child_menu) {
      parent_srno = this.sub_parent_child_menu.id
    }
  var data =
    {
      name: this.data.name,
      description: this.data.description,
      srno:this.data.srno,
      parent_srno:parent_srno
    }
  if(this.validationforHelp()){
  this.http.post(environment.apiURL + 'updateHelp',data, { responseType: 'text' }).subscribe(response => {
    this.hideDialog()
    this.getData();
    this.messageService.add({severity:'success', detail: response});
  },error => {
    this.messageService.add({ severity: 'error', detail: error.error });
  });
  }
  }
  pathManager(id){
      for (var i = 0; i < this.helpDetails.length; i += 1) {
        if(id==this.helpDetails[i].srno){
          if(this.helpDetails[i].parentsrno==0){
            this.parent=this.helpDetails[i]
          }
          else{
            if(this.sub_parent_child==null){
              this.sub_parent_child=this.helpDetails[i]
            }
            else{
              this.sub_parent=this.helpDetails[i]
            }
            this.pathManager( this.helpDetails[i].parentsrno )
          }
        }
      }
  }


  validationforHelp() { 
    var flaggeneralvalidate = false;
    if (this.data.name == null || this.data.name.length == 0) {
      this.messageService.add({ severity: 'error', detail: 'Please enter Menu Name' });
    } else if(!this.tssvcommonvalidatorService.isRegexValid("alphanumericspace", this.data.name)){
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in Menu Name 0-9 a-z A-Z <space>' });
    } else if(this.data.name.length > 100){
      this.messageService.add({ severity: 'error', detail: 'The length of Menu Name should be less than 100 character' });
    } else if (this.data.description == null || this.data.description.length < 4) {
      this.messageService.add({ severity: 'error', detail: 'Please enter valid Description' });
    } else {
      flaggeneralvalidate = true;
    }
    return flaggeneralvalidate;
  }

}


