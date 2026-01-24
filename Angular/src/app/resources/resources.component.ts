import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({

  selector: 'app-resources',
  templateUrl: './resources.component.html',
  styleUrls: ['./resources.component.scss']

})

export class ResourcesComponent implements OnInit {

  resources;
  constructor(private http:HttpClient,
    private router:Router,
    private route: ActivatedRoute) { }
    parent;
    srno;
    data;
    id
   heading;
    items;
  ngOnInit(): void {


    this.srno = this.route.snapshot.params['srno'];
    this.http.get<any>(environment.apiURL + 'resources').subscribe(response => {
        this.resources=response;
        if(this.srno==null){
          for(var i = 0; i < this.resources.length; i++){
            this.getChild(this.resources[i].srno)
          }
        }
        if(this.srno!=null){
          for (var i = 0; i < this.resources.length; i++) {
            if (this.resources[i].srno ==this.srno ) {
             this.heading = this.resources[i].guidelines
            }
          }
          this.http.get(environment.apiURL + 'getchild/' + this.srno).subscribe(response => {
            this.items=response;
          });
        }
    })



  }
  getChild(e)
  {
    this.http.get(environment.apiURL + 'getchild/' + e ).subscribe(response => {
      this.items=response;
      var item = { srno: Number, guidelines:'',content:''};
      var arr=[]
      var parent;
      for(var i =0; i<this.resources.length; i++){
        if(e==this.resources[i].srno){
          parent=this.resources[i]
        }
      }
      if(response!=null)
            {
            
              item.srno=parent.srno
              item.guidelines=parent.guidelines
              item.content='true'
              this.parent.push(item)

            }
            else{
              item.srno=parent.srno
              item.guidelines=parent.guidelines
              item.content='false'
              this.parent.push(item)

            }
          
    });
  }
  getdata(id){
    this.router.navigate(["/Resources/" + id ]);
  }
}
