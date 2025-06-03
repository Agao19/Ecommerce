import { EventEmitter, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Token } from '@angular/compiler';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  authStatuschanged = new EventEmitter<void>();

  private static BASE_URL = `http://localhost:8080`;

  constructor(private http: HttpClient) { }

  private getHeader(): HttpHeaders{
    const token=localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bear: ${token}`,
      "Content-Type": "application/json"
    });
  }


  // AUTH & USERS API METHODS

  registerUser(registration: any): Observable<any>{
    return this.http.post(`${ApiService.BASE_URL}/auth/register`,registration);
  }

  loginUser(loginDetails: any): Observable<any>{
    return this.http.post(`${ApiService.BASE_URL}/auth/login`,loginDetails);
  }

  
  geLoggedInUserInfo(): Observable<any>{
    return this.http.get(`${ApiService.BASE_URL}/user/my-info`,{
      headers: this.getHeader()
    });
  }

  //PRODUCTS API
  








}
