import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-blogs',
  templateUrl: './blogs.component.html',
  styleUrls: ['./blogs.component.scss']
})
export class BlogsComponent implements OnInit {

  blogs:any;
  tags:any;
  blogtemp;
  category;
  data;

  con="All Category";
  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<any>(environment.apiURL + 'blogs').subscribe(response => {
      this.blogs=response.content;
      this.blogtemp=response.content;
      for (var i=0;i<this.blogtemp.length;i++){
        const myArray = this.blogtemp[i].category.split(",");
        this.con=this.con.concat(",",myArray)
      }
      var arr=this.con.split(",")
      this.tags = arr.filter((v, i, a) => a.indexOf(v) === i);
      this.blogs.sort((a, b) => b.createdate - a.createdate)
    });

  }
  sort(){
    if(this.category==null || this.category=="All Category"){
      this.blogs=this.blogtemp
    }
    else{
      this.blogs=[];
      for (var i=0;i<this.blogtemp.length;i++){
        if(this.blogtemp[i].category.includes(this.category)){
          this.blogs.push(this.blogtemp[i])
        }
      }
    }

  }

  

}
