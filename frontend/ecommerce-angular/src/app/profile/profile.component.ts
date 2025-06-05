import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PaginationComponent } from '../pagination/pagination.component';
import { ApiService } from '../service/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, PaginationComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  constructor(private apiService: ApiService, private router:Router) {}

  userInfo: any = null;
  error:any = null;
  currentPage: number = 1;
  itemsPerpage:number =5;

  ngOnInit(): void {
    this.fetchUserInfo();
  }

  fetchUserInfo():void{
    this.apiService.getLoggedInUserInfo().subscribe({
      next: (response) => {
        this.userInfo = response.user
      },
      error: (error) => {
        console.log(error)
        this.error = error?.error?.message || "Unable to fetch user informatio"
      }
    })
  }

  handleAddressClick():void{
    const urlPathToNavigateTo = this.userInfo?.address ? '/edit-address' : '/add-address'
    this.router.navigate([urlPathToNavigateTo])
  }

  onPageChange(page: number): void{
    this.currentPage = page;
  }

  get paginatedOrders(): any[]{
    if(!this.userInfo?.orderItemlist) return[];
    
    return this.userInfo.orderItemlist.slice(
      (this.currentPage-1)*this.itemsPerpage,
      this.currentPage * this.itemsPerpage
    )
  }

  get totalPages():number {
    return Math.ceil((this.userInfo?.orderItemlist?.length || 0) / this.itemsPerpage)
  }
}
