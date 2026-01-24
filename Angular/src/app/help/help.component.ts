import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Dropdown } from 'primeng/dropdown';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Router } from '@angular/router';
import * as Highcharts from 'highcharts';
import wordCloud from "highcharts/modules/wordcloud.js";
wordCloud(Highcharts);

import { environment } from '../../environments/environment';
import { TssvcommonvalidatorService } from '../services/tssvcommonvalidator.service';

@Component({
  selector: 'app-help',
  templateUrl: './help.component.html',
  styleUrls: ['./help.component.css']
})
export class HelpComponent implements OnInit {

  @ViewChild('dd') dropdown: Dropdown;

  help: any;
  helpPages: any;
  path = "";
  SelectedSection: any;
  catg: any[] = [];
  items: MenuItem[];
  home: MenuItem;
  helpDetails: any;
  model: any[];
  temp: any[];
  url = [];
  counter = 0;
  data: any;
  querydialog:boolean;
  QueryField = {
    'query': {},
  }
  query;
  Highcharts: typeof Highcharts = Highcharts;
  chartOptions: Highcharts.Options = {
    series: [{
     turboThreshold:20000,
      type: 'wordcloud'
    }]

  };

  constructor(private http: HttpClient,private messageService: MessageService, private tssvcommonvalidatorService: TssvcommonvalidatorService,
    private router: Router) {}

  ngOnInit(): void {

    this.QueryField.query=null
    this.getWordCloud();
    this.http.get<any>(environment.apiURL + 'help').subscribe(response => {
      this.help = response.content;

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

    this.home = { icon: 'pi pi-book', label: ' Documentation' };
  }

  setValue(subpage: any, subsection: any, section: any, page: any) {

    var items = [];
    items.push({ label: page.label })
    if (section == '') {
      this.SelectedSection = page;
    } else if (subsection == '') {
      this.SelectedSection = section
      items.push({ label: section.label })

    }
    else if (subpage == '') {
      this.SelectedSection = subsection
      items.push({ label: section.label })
      items.push({ label: subsection.label })
    }
    else {
      this.SelectedSection = subpage;

      items.push({ label: section.label })
      items.push({ label: subsection.label })
      items.push({ label: subpage.label })

    }
    this.items = items
    this.addWord(this.SelectedSection.label);
  }

  ItemClick(e: any) {
    if (e.item.label == ' Documentation') {
      this.SelectedSection = '';
      this.items = null;
    }
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
  SearchData(e){
    if(e.length>3){
      this.temp = [];
      for(var i=0;i<this.helpDetails.length;i++){
        if(this.helpDetails[i].description!=null){
        if(this.helpDetails[i].description.toLowerCase().includes(e.toLowerCase()) || this.helpDetails[i].name.toLowerCase().includes(e.toLowerCase())){
            this.temp.push(this.help[i].name)
        }
        }
      }

    }
  }
  clean()
  {
    this.temp=null
  }
  setData(e){
    var SelectedSection = [];

    for(var i=0;i<this.helpDetails.length;i++){
      if(this.helpDetails[i].name==e){
        var item1 = { id: Number, label: '', order_no: Number, parentid: Number, description: '' };
        item1.id = this.helpDetails[i].srno;
        item1.label = this.helpDetails[i].name;
        item1.order_no = this.helpDetails[i].order_no;
        item1.parentid = this.helpDetails[i].parentsrno;
        item1.description = this.helpDetails[i].description;
        SelectedSection.push(item1);
        this.SelectedSection=SelectedSection[0]
      }
    }

    this.pathManager(this.SelectedSection.parentid);
  }
  addWord(e){
    var model =
      {
        word:e
      }
      this.http.post(environment.apiURL + 'search',model, { responseType: 'text' }).subscribe(response => {
        this.getWordCloud();
      });
  }
  change(e){
    if(e.originalEvent.type=='click'){
      this.addWord(e.value);
      this.setData(e.value);
    }
  }
  pathManager(id){
    this.items = [];
    this.url = [];
    this.url.push({ label: this.SelectedSection.label });
    for (var i = 0; i < this.helpDetails.length; i += 1) {
      if(id==this.helpDetails[i].srno){
        if(this.helpDetails[i].parentsrno==0){
          this.url.push({ label: this.helpDetails[i].name });
        }
        else{
          this.url.push({ label: this.helpDetails[i].name });
          this.pathManager( this.helpDetails[i].parentsrno )
        }
      }
    }
    this.items=this.url.reverse()
  }
  getWordCloud(){
    this.http.get<any>(environment.apiURL + 'wordcloud').subscribe(response => {
      var data=response.content;
      var main =[]
      for(var i=0;i<data.length;i++){
        main.push({name:data[i].word,weight:data[i].count,color:'#'+(0x1000000+(Math.random())*0xffffff).toString(16).substr(1,6)});
      }
      this.data=main;
      this.loadgraph();
    });
  }
  loadgraph()
  {
    this.chartOptions  = {
      series: [{
        turboThreshold:20000,
        minFontSize:5,
        type: 'wordcloud',
        data: this.data,
        name: 'Searched',
        rotation: {
          from: 0,
          to: 0
        },
        spiral: 'square',
        point:{
          events:{
            click: function(e) {
              this.setData(e.point.options.name);
            }.bind(this)
          }
        },

        states:{
          hover:{
            brightness:0.1,
            color:"black"
          }
        }
      }],
      credits: {
        enabled: false
      },
      legend: {
        enabled:true
      },
      tooltip:{
        enabled:true

      },
      title:{
        text:''
      }
    }
  }

Save() {
  this.QueryField.query=this.query
  if(this.validationQuery()) {
  this.http.post(environment.apiURL + 'PostQuery',this.QueryField, {responseType: 'text'}).subscribe(response => {
  this.data = response;
  this.QueryField.query=null;
  this.router.navigate(["/Help"]);
  this.hideDialog()
  this.messageService.add({ severity: 'success', summary: 'Success', detail: "Query has been added successfully." });
  }, error => {
    this.hideDialog()
    this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error });
  });
}
}
QueryPost(){
 this.querydialog = true;
}
hideDialog() {
 this.querydialog = false;
 this.query=null;

}

validationQuery() {

  var flaggeneralvalidate = false;

    if (this.query == null || this.query.trim() == '' ) {

      this.messageService.add({ severity: 'error', summary:'Error', detail: 'Please enter query' });

    }
    else if(this.query.length > 100){
      this.messageService.add({ severity: 'error', summary:'Error', detail: 'The length of Query should be less than 100 characters' });
    }
 else if(!this.tssvcommonvalidatorService.isRegexValid("Markdownregx", this.query)){
      this.messageService.add({ severity: 'error', detail: 'Only following characters are allowed in Query a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@' });
    }
    else {

      flaggeneralvalidate = true;

    }

    return flaggeneralvalidate;

}


}
