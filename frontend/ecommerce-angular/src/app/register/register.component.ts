import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../service/api.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-register',
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  constructor(private router: Router, private readonly apiService: ApiService) {}
  formData: any={
    email: '',
    name: '',
    phoneNumber: '',
    password: '',
  }

  message: any = null;

  async handleSubmit(){
    if(!this.formData.email || !this.formData.name || !this.formData.phoneNumber || !this.formData.password){
      this.message = 'Please fill all the fields';
    }
    try {
      const response: any= await firstValueFrom(this.apiService.registerUser(this.formData));
      if(response.status ===200){
        this.message = 'Registration successful';
        this.router.navigate(['/login']);
      }
    } catch (error: any) {
      console.log(error);
      this.showMessage(error.error?.message || error.message || 'unable to register');
    }
  }

  showMessage(message: string){
    this.message = message;
    setTimeout(()=>{
      this.message = null;  
    }, 3000);
  }
















  
}
