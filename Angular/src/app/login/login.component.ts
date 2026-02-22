import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { environment } from '../../environments/environment';
import { IndexService } from '../index.service';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [BrowserModule,CommonModule,FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  apiUrl = environment.url

  userId: string = '';
  password: string = '';

  constructor(private http: HttpClient,private service: IndexService,private router:Router,    private messageService: MessageService,
) {}

ngOnInit() {
    this.service.isAuthUser().subscribe(async (data:any) => {
      await this.router.navigateByUrl('', { replaceUrl: true });
      window.location.reload();
    });
  }



onSubmit() {
    const payload = {
      userId: this.userId,
      password: this.password
    };
    let flag=0;
      if(this.password.trim().length==0){
        this.messageService.add({severity: 'error', detail: ' Password is empty' });
        return;
      }
      if(this.userId.trim().length==0){
        this.messageService.add({severity: 'error', detail: ' User ID is empty' });
        return;
      }


    this.http.post(this.apiUrl+'/dologin', payload).subscribe({
      next: async (response) => {
          await this.router.navigateByUrl('', { replaceUrl: true });
          window.location.reload();
          this.messageService.add({ severity: 'success', detail: 'Logged In Successfully' });

      },
      error: (err) => {
        if(err.status==401){
        this.messageService.add({ severity: 'error', detail: 'Wrong credentials' });
      }
    }
    });
  }

}
