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
        console.log('User Info Response:', response);
        this.userInfo = response.user;
        console.log('User Info after assignment:', this.userInfo);
        console.log('Order Item List:', this.userInfo?.orderItemList);
      },
      error: (error) => {
        console.log('Error:', error)
        this.error = error?.error?.message || "Unable to fetch user information"
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
    if(!this.userInfo?.orderItemList) return[];
    
    return this.userInfo.orderItemList.slice(
      (this.currentPage-1)*this.itemsPerpage,
      this.currentPage * this.itemsPerpage
    )
  }

  get totalPages():number {
    return Math.ceil((this.userInfo?.orderItemList?.length || 0) / this.itemsPerpage)
  }
}
