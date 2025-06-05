import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ApiService } from '../service/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-category',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './category.component.html',
  styleUrl: './category.component.css'
})

export class CategoryComponent implements OnInit{

  error:any = null;
  categories: any[]= [];

  constructor(private apiService: ApiService, private router: Router){}
  ngOnInit(): void {
    this.fetchCatogires();
  }

  fetchCatogires(): void{
    this.apiService.getAllCategories().subscribe({
      next: (response) => {
        this.categories=response.categoryList || []
      },
      error: (err) => {
        this.error = err?.error.message || 'unable to get categories'
      }
    })
  }
  handleCategoryClick(categoryId: number): void{
    this.router.navigate(['/products', categoryId])
  }
}
