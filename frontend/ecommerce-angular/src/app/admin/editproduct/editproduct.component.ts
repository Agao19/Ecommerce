import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../service/api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-editproduct',
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './editproduct.component.html',
  styleUrl: './editproduct.component.css'
})
export class EditproductComponent implements OnInit{
  constructor(private apiService: ApiService, private router:Router, private route:ActivatedRoute, private fb:FormBuilder){}

  editProductForm!: FormGroup;
  categories: any[] = [];
  message: any=null;
  imageUrl: any = null;
  productId: string = '';

  ngOnInit(): void {
    this.productId = this.route.snapshot.paramMap.get('productId') || ''
    this.editProductForm = this.fb.group({
      image: [null],
      categoryId: [''],
      name: [''],
      description: [''],
      price: ['']
    })

    this.apiService.getAllCategories().subscribe(res =>{
      console.log('Categories Response:', res);
      this.categories = res.categoryList; //this.categories sử dụng trong template HTML
    })

    if(this.productId){//productId => lấy từ url
      this.apiService.getProductByProductId(this.productId)
      .subscribe(response =>{
        console.log('Product Response:', response);
        this.editProductForm.patchValue({
          categoryId: response.product.categoryId, //response ở đây là response từ getProductByProductId
          name: response.product.name,
          description: response.product.description,
          price: response.product.price
        });
        this.imageUrl = response.product.imageUrl;
      })
    }
  }

  handleImageChange(event: Event): void{
    const input = event.target as HTMLInputElement;

    if(input.files && input.files[0]){
      const file = input.files[0];
      this.editProductForm.patchValue({image: file});

      const reader = new FileReader();

      reader.onload = () => {
        this.imageUrl = reader.result as string 
      }
      reader.readAsDataURL(file)
    }
  }

  handleSubmit():void {
    const formData = new FormData();
    const formValues = this.editProductForm.value;

    if(formValues.image){
        formData.append('image', formValues.image)
    }

    formData.append('productId',this.productId)
    formData.append('categoryId',formValues.categoryId)
    formData.append('name',formValues.name)
    formData.append('description',formValues.description)
    formData.append('price',formValues.price)

    console.log("Product ID: " + this.productId)
    console.log("categoryId ID: " + formValues.categoryId)
    console.log("NAme : " + formValues.name)
    console.log("Des : " + formValues.description)
    console.log("Price: " + formValues.price)

    this.apiService.updateProduct(formData).subscribe({
      next: (res) =>{
        this.message=res.message
        setTimeout(()=>{
          this.message = ''
          this.router.navigate(['/admin/products'])
        },3000)
      },
      error: (error) =>{
        console.log(error)
        this.message=error?.error?.message || "Unable update product"
      }
    })
  }

}
