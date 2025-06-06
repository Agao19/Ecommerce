import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../service/api.service';
import { Router } from '@angular/router';
import { errorContext } from 'rxjs/internal/util/errorContext';

@Component({
  selector: 'app-addproduct',
  imports: [CommonModule, FormsModule],
  templateUrl: './addproduct.component.html',
  styleUrl: './addproduct.component.css'
})
export class AddproductComponent implements OnInit{
  constructor(private apiService: ApiService, private router:Router){}
  image: any = null
  categories: any[] = []
  categoryId: number | null = null
  name: string = ''
  description: string = ''
  price: string = ''
  message: string = ''


  ngOnInit(): void {
    this.apiService.getAllCategories().subscribe({
      next: (response)=>{
        this.categories=response.categoryList || []
      },
      error: (err)=>{
        console.log(err)
      }
    })
  }

  handleImage(event: Event):void{
    const input= event.target as HTMLInputElement;
    if(input.files && input.files.length > 0){
      this.image=input.files[0]
    }
  }

  handleSubmit():void{
    if (!this.image || !this.categoryId || !this.name || !this.description || !this.price) {
      this.message="Please fill in all fields";
      return;
    }
    const formData = new FormData();
      formData.append('image',this.image);
      formData.append('categoryId',this.categoryId.toString());
      formData.append('name',this.name);
      formData.append('description',this.description);
      formData.append('price',this.price);


      this.apiService.addProduct(formData).subscribe({
        next: (res)=>{
          console.log('Add Product Response:', res);
          this.message = res.message;
          setTimeout(()=>{
            this.router.navigate(['/admin.products'])
          },3000)
        },
        error: (err) => {
          console.log(err)
          this.message=err?.error?.message || "Unable to save a product"
        }
      })
  }
}
