import { inject, Inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { ApiService } from './api.service';


export const userGuard: CanActivateFn=(route,sate) =>{
  if(inject(ApiService).isAuthenticated()){
    return true;
  }else{
    inject(Router).navigate(['/login'])
    return false;
  }
}

export const adminGuard: CanActivateFn=(route,sate) =>{
  if(inject(ApiService).isAdmin()){
    return true;
  }else{
    inject(Router).navigate(['/login'])
    return false;
  }
}