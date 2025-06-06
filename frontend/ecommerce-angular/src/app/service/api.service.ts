import { EventEmitter, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Token } from '@angular/compiler';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  authStatuschanged = new EventEmitter<void>();

  private static BASE_URL = `http://localhost:3030`;

  constructor(private http: HttpClient) { }

  private getHeader(): HttpHeaders{
    const token=localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }


  // AUTH & USERS API METHODS

  registerUser(registration: any): Observable<any>{
    return this.http.post(`${ApiService.BASE_URL}/auth/register`,registration);
  }

  loginUser(loginDetails: any): Observable<any>{
    return this.http.post(`${ApiService.BASE_URL}/auth/login`,loginDetails);
  }

  
  getLoggedInUserInfo(): Observable<any> {
    return this.http.get(`${ApiService.BASE_URL}/user/my-info`, {
      headers: this.getHeader()
    })
  }

  //PRODUCTS API
  addProduct(body: any): Observable<any>{
    return this.http.post(`${ApiService.BASE_URL}/product/create`,body,{
      headers: this.getHeader()
    });
  }

  updateProduct(body: any): Observable<any>{
    return this.http.put(`${ApiService.BASE_URL}/product/update`,body,{
      headers: this.getHeader()
    });
  }

  searchForProduct(body: any): Observable<any>{
    return this.http.get(`${ApiService.BASE_URL}/product/search`,{
      params: {body},

    });
  }

  getAllProducts(): Observable<any>{
    return this.http.get(`${ApiService.BASE_URL}/product/get-all`,{
    });
  }
  
  getProductByProductId(productId: string): Observable<any>{
    return this.http.get(`${ApiService.BASE_URL}/product/get-by-product-id/${productId}`,{
     
    });
  }
  
  getProductsByCategoryId(categoryId: string): Observable<any>{
    return this.http.get(`${ApiService.BASE_URL}/product/get-by-category-id/${categoryId}`,{
     
    });
  }

  deleteProduct(productId: string): Observable<any>{
    return this.http.delete(`${ApiService.BASE_URL}/product/delete/${productId}`,{
      headers: this.getHeader()
    });
  }


  //CATEGORY API
  createCategory(body: any): Observable<any>{
    return this.http.post(`${ApiService.BASE_URL}/category/create`,body,{
      headers: this.getHeader()
    });
  }

  updateCategory(categoryId: string, body: any): Observable<any>{
    return this.http.put(`${ApiService.BASE_URL}/category/update/${categoryId}`,body,{
      headers: this.getHeader()
    });
  }
  getAllCategories(): Observable<any>{
    return this.http.get(`${ApiService.BASE_URL}/category/get-all`)
  }
  getByCategoryId(categoryId: string): Observable<any>{
    return this.http.get(`${ApiService.BASE_URL}/category/get-category-by-id/${categoryId}`,{
    });
  }

  deleteCategory(categoryId: string): Observable<any>{
    return this.http.delete(`${ApiService.BASE_URL}/category/create/${categoryId}`,{
      headers: this.getHeader()
    });
  }

  //Order API 
  
  createOrder(body: any): Observable<any>{
    return this.http.post(`${ApiService.BASE_URL}/order/create`,body,{
      headers: this.getHeader()
    });
  }

  updateOrderItemStatus(status: string, orderItemId: string, body: any = {}): Observable<any>{
    return this.http.put(`${ApiService.BASE_URL}/order/update-item-status/${orderItemId}`,body,{
      headers: this.getHeader(),
      params: {status}
    });
  }

  getAllOrders(): Observable<any>{
    return this.http.get(`${ApiService.BASE_URL}/order/filter`,{
      headers: this.getHeader()
    });
  }

  
  getORderItemById(itemId: string): Observable<any>{
    return this.http.get(`${ApiService.BASE_URL}/order/filter`,{
      headers: this.getHeader(),
      params: {itemId}
    })
  }
  getAllOrdersItemByStatus(status: string): Observable<any>{
    return this.http.get(`${ApiService.BASE_URL}/order/filter`,{
      headers: this.getHeader(),
      params: {status}
    });
  }

  deleteOrderItems(orderItemId: string): Observable<any>{
    return this.http.delete(`${ApiService.BASE_URL}/category/create/${orderItemId}`,{
      headers: this.getHeader()
    });
  }

  //Adderss api
  saveAddress(body: any): Observable<any>{
    return this.http.post(`${ApiService.BASE_URL}/address/save`,body,{
      headers: this.getHeader()
    });
  }

  //Authentication
  logout(): void{
    localStorage.removeItem('token');
    localStorage.removeItem('role');
  }

  isAuthenticated(): boolean{
    const token=localStorage.getItem('token')
    return !!token;
  }

   isAdmin(): boolean{
    const role=localStorage.getItem('role')
    return role ==='ADMIN';
  }



  // saveToken(token: string): void{
  //   localStorage.setItem("token", token);
  // }

  // getToken(): string | null{
  //   const token=localStorage.getItem("token");
  //   return token;
  // }

  
}
