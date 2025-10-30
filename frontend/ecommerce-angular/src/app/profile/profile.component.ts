import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PaginationComponent } from '../pagination/pagination.component';
import { ApiService } from '../service/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, PaginationComponent, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {

  constructor(private apiService: ApiService, private router: Router) { }

  userInfo: any = null;
  error: any = null;
  currentPage: number = 1;
  itemsPerpage: number = 5;

  // Password creation form
  showPasswordForm: boolean = false;
  passwordFormData = {
    password: '',
    confirmPassword: ''
  };
  passwordError: string = '';
  passwordSuccess: string = '';
  isSubmittingPassword: boolean = false;

  ngOnInit(): void {
    this.fetchUserInfo();
  }

  fetchUserInfo(): void {
    this.apiService.getLoggedInUserInfo().subscribe({
      next: (response) => {
        this.userInfo = response.user;
        // Hiển thị form tạo password nếu user chưa có password
        this.showPasswordForm = this.userInfo?.noPassword === true;
      },
      error: (error) => {
        console.log('Error:', error);
        this.error = error?.error?.message || "Unable to fetch user information";
      }
    });
  }

  /**
   * ✅ Submit form tạo password
   */
  createPassword(): void {
    this.passwordError = '';
    this.passwordSuccess = '';

    // Validate
    if (!this.passwordFormData.password || !this.passwordFormData.confirmPassword) {
      this.passwordError = 'Please enter password and confirm password';
      return;
    }

    if (this.passwordFormData.password !== this.passwordFormData.confirmPassword) {
      this.passwordError = 'Passwords do not match';
      return;
    }

    if (this.passwordFormData.password.length < 8) {
      this.passwordError = 'Password must be at least 8 characters';
      return;
    }

    // Call API
    this.isSubmittingPassword = true;

    this.apiService.createPasswordForGoogleUser(this.passwordFormData.password)
      .subscribe({
        next: (response) => {
          console.log('Password created:', response);
          this.passwordSuccess = 'Password created successfully! You can now login with email and password.';

          // Reset form
          this.passwordFormData.password = '';
          this.passwordFormData.confirmPassword = '';

          // Reload user info
          setTimeout(() => {
            this.fetchUserInfo();
          }, 2000);
        },
        error: (error) => {
          console.error('Error creating password:', error);
          this.passwordError = error.error?.message || 'Failed to create password. Please try again.';
        },
        complete: () => {
          this.isSubmittingPassword = false;
        }
      });
  }

  /**
   * ✅ Close password form
   */
  closePasswordForm(): void {
    this.showPasswordForm = false;
    this.passwordFormData.password = '';
    this.passwordFormData.confirmPassword = '';
    this.passwordError = '';
    this.passwordSuccess = '';
  }

  handleAddressClick(): void {
    const urlPathToNavigateTo = this.userInfo?.address ? '/edit-address' : '/add-address';
    this.router.navigate([urlPathToNavigateTo]);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
  }

  get paginatedOrders(): any[] {
    if (!this.userInfo?.orderItemList) return [];

    return this.userInfo.orderItemList.slice(
      (this.currentPage - 1) * this.itemsPerpage,
      this.currentPage * this.itemsPerpage
    );
  }

  get totalPages(): number {
    return Math.ceil((this.userInfo?.orderItemList?.length || 0) / this.itemsPerpage);
  }
}