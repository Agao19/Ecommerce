import { Component, OnDestroy, OnInit } from '@angular/core';
import { ApiService } from '../service/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';



@Component({
  selector: 'app-navbar',
  imports: [CommonModule,FormsModule,RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit, OnDestroy{

  constructor(private readonly apiService: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.isAuthenticated=this.apiService.isAuthenticated();
    this.isAdmin=this.apiService.isAdmin();

    //lang nge su kien subscribe de xuong detroys cho de
    this.authStatusSub=this.apiService.authStatuschanged.subscribe(
      ()=>{
      this.isAuthenticated=this.apiService.isAuthenticated(); //callback sẽ xác thực lại
      this.isAdmin=this.apiService.isAdmin();
    })
  }

  handleSearchSubmit(){
    this.router.navigate(['/home'], {
      queryParams: {search: this.searchValue}
    });
  }

  handleLogout(){
    const confirm=window.confirm("Are you sure you want to logout")
    if(confirm){
      this.apiService.logout();
      this.router.navigate(['/home']);
      this.apiService.authStatuschanged.emit();
    }
  }

  ngOnDestroy(): void {
    if(this.authStatusSub){
      this.authStatusSub.unsubscribe();
    }
  }

  isAdmin: boolean = false;

  isAuthenticated: boolean = false;

  searchValue: string = '';
  private authStatusSub: Subscription | null = null;
  
  
}
