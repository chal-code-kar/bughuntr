import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { environment } from '../../environments/environment';



@Component({
  selector: 'app-worklist',
  templateUrl: './worklist.component.html',
  styleUrls: ['./worklist.component.scss']
})
export class WorklistComponent implements OnInit {

  workList = {};
  category = [];
  selectedList = [];
  selectedCategoryList = [];
  selectedCategory;
  filter = [];
  selectedFilter = [];

  constructor(private http:HttpClient, private messageService:MessageService, private router:Router) { }

  ngOnInit(): void {
    this.http.get(environment.apiURL + 'worklists').subscribe(response => {
      this.workList = response;
      if(Object.keys(this.workList).length == 0){
        this.messageService.add({ severity: 'success', detail: 'No work History found'});
        this.router.navigate(["/"]);
      }
      this.category.push('All');
      this.category = this.category.concat(Object.keys(this.workList));
      this.selectedCategory = this.category[0];
      var All = [];
      for(var i=1;i<this.category.length;i++){
        All = All.concat(this.workList[this.category[i]]);
        this.workList[this.category[i]] = this.sortData(this.workList[this.category[i]]);
      }
      this.workList['All'] = All;
      this.selectedList = this.sortData(All);

      for(var j=0;j<this.selectedList.length;j++){
        if(j<10){
          this.selectedCategoryList.push(this.selectedList[j])
        }
        if(!this.filter.includes(this.selectedList[j]['filter'])){
          this.filter.push(this.selectedList[j]['filter']);
        }
      }
    })
  }

  changeMenu(category){
    this.selectedFilter = [];
    this.selectedCategory = category;
    this.selectedList = this.workList[category];
    
    this.filter = [];
    for(var j=1;j<this.selectedList.length;j++){
      if(j<10){
        this.selectedCategoryList.push(this.selectedList[j])
      }
      if(!this.filter.includes(this.selectedList[j]['filter'])){
        this.filter.push(this.selectedList[j]['filter']);
      }
    }
  }

  sortData(data) {
    for (var i = 0; i < data.length; i++) {
      
      for (var j = i + 1; j < data.length; j++) {
          var temp = 0;
          var temp1 = data[j].createddt;
          if ( data[j].createddt > data[i].createddt) {
              
              temp = data[i];
              data[i] = data[j];
              data[j] = temp;
          }
      }
    }
    return data;
  }

  changeFilter(){
    this.selectedCategoryList = [];
    this.selectedList = this.workList[this.selectedCategory];
    if(this.selectedFilter.length == 0){
      return;
    } else {
      var list = [];
      for(var i=0;i<this.selectedList.length;i++){
        if(this.selectedFilter.includes(this.selectedList[i].filter)){
          list.push(this.selectedList[i])
        }
      }
    this.selectedList = list;
    }
  }

}
