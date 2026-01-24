import { MenuItem } from './menu-item';
import { Component, OnInit } from '@angular/core';
import { Globals } from './globals';
import { IndexService } from './index.service';
import { Router } from '@angular/router';
 
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
 
    headers: any;
    login:boolean=false;
    userInfo: any;
    roles: any[];
    menuList : MenuItem[];
    parentNodes : MenuItem[]=[];
    toDisplay = false;
 
    constructor(private service: IndexService, private globals: Globals,private router:Router) { }
    ngOnInit() {
      this.service.isAuthUser().subscribe(()=>{
        this.service.getMenus().subscribe((data:any) => {
          this.globals.isAuthenticated = true;
          this.toDisplay = true;
        const keys = data.headers.keys();
        this.headers = keys.map(key =>
          `${key}: ${data.headers.get(key)}`);
          this.userInfo = data.body.brandingDetails;
          this.userInfo.roles = data.body.roles;
           this.userInfo.profile=data.body.profile;
          this.menuList = data.body.menuList;
 
          this.menuList = this.list_to_tree(this.menuList);
          this.menuList = this.removeItems(this.menuList);
 
          this.menuList.sort((a, b) => {
            return a.id - b.id;
          });
 
          this.menuList.forEach(menu =>{
            if(menu.text.includes(':')){
              var arr = menu.text.split(':');
              menu.icon = arr[0];
              menu.text = arr[1];
            } else {
              menu.icon='';
            }
          })
 
          this.globals.roles = data.body.roles;
          let env : boolean = data.body.SM_ENV;
          if(!env) this.globals.empId=this.userInfo.ID;
          this.globals.roles=this.userInfo.roles;
          this.globals.profile=this.userInfo.profile;
          
          if(this.menuList !== null && this.menuList.length>0)
          {
            
            for(let obj of this.menuList)
             {
               if(obj.parentid === 0)
               {
                this.parentNodes.push(obj);
               }
             }
             
             
             for (let menuItem of this.parentNodes)
             {
             this.buildTreeviewMenu(menuItem, this.menuList);
             }
 
          }
          this.login=true
        });
     
     
      });
 
    }
 
 
    public buildTreeviewMenu(menuItem : MenuItem, menudata : MenuItem[]):void
    {
      
      menuItem.subMenus=[];
      let menuItems = new Array<MenuItem>();
 
      
      
      for(let obj of menudata)
           {
             if(obj.parentid === menuItem.id)
             {
              menuItems.push(obj);
             }
           }
 
      
      if(menuItems !== null && menuItems.length>0)
      {
        
        
        for (let item of menuItems)
        {
          menuItem.subMenus.push(item);
          this.buildTreeviewMenu(item, menudata);
        }
      }
    }
 
    public onNoClick(){
      this.globals.view=!this.globals.view;
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
          
          list[map[node.parentid]].items.push(node);
        } else {
          roots.push(node);
        }
      }
 
      function mycomparator(a,b) {
        return parseInt(a.price, 10) - parseInt(b.price, 10);
      }
      roots.sort(mycomparator);
 
      return roots;
  }
 
 
  removeItems(array){
      for (var i=0;i<array.length;i++){
          if(array[i].items.length == 0){
              delete array[i].items;
          }else{
              this.removeItems(array[i].items);
          }
      }
      return array;
  }
 
  }