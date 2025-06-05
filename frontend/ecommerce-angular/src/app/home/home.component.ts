import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductlistComponent } from '../productlist/productlist.component';
import { PaginationComponent } from '../pagination/pagination.component';
import { ApiService } from '../service/api.service';
import { ActivatedRoute, Router, ParamMap } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [CommonModule, ProductlistComponent, PaginationComponent],
  standalone: true,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent  implements OnInit{
  constructor(private apiService: ApiService, private router:Router, private route:ActivatedRoute){}
  
  products: any[] = [];
  currentPage = 1;
  totalPages=0;
  itemsPerPage=8;
  error: any=null;

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params: ParamMap)=>{
      const searchItem=params.get('search');
      const pageParam=params.get('page');
      this.currentPage = pageParam ? +pageParam : 1
      this.fetchProducts(searchItem);
    })
  }
  fetchProducts(searchItem: string | null): void{
    const productObservable = searchItem ? this.apiService.searchForProduct(searchItem)
    :this.apiService.getAllProducts();

    productObservable.subscribe({
      next:(response)=>{
        if(response?.productList && response.productList.length > 0){
          this.handleProductResponse(response.productList)
        }else {
          this.error = 'Product Not Found'
        }
      },
      error:(error) =>{
        console.log(error)
        this.error = error?.error?.message || "error getting products";
      }
    })
  }

  handleProductResponse(products: []): void{
    this.totalPages = Math.ceil(products.length/this.itemsPerPage);
    this.products=products.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    )
  }

  changePage(page: number): void{
    this.router.navigate([],{
      relativeTo: this.route,
      queryParams: {page},
      queryParamsHandling: 'merge'
    }
      
    )
  }
}
