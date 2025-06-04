import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule,FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  constructor(private router:Router, private apiService: ApiService){}
  formData: any= {
    email: '',
    password: ''
  }
  message: any = null;

async handleSubmit(){
    if (!this.formData.email || !this.formData.password) {
      this.showMessage("Email and Password are required")
      return;
    }
    try {
      const response: any = await firstValueFrom(this.apiService.loginUser(this.formData));
      if (response.status===200) {
        this.showMessage("User successfully logged in");
        localStorage.setItem('token',response.token)
        localStorage.setItem('role',response.role)
        this.apiService.authStatuschanged.emit();
        this.router.navigate(['/profile']);
      }
    } catch (error: any) {
      console.log(error);
      this.showMessage(error.error?.message || error.message || 'unable to login');
    }
  }

  showMessage(message: string){
    this.message=message;
    setTimeout(() => {
      this.message==null
    }, 3000);
  }

}
