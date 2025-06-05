import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../service/api.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-adminorderdetails',
  imports: [CommonModule, FormsModule],
  templateUrl: './adminorderdetails.component.html',
  styleUrl: './adminorderdetails.component.css'
})
export class AdminorderdetailsComponent implements OnInit{
  orderItem: any[] = [];
  selectedStatus: {[key:number]: string} = {};
  orderId: any = ''
  message: any = null;
  OrderStatus = ["PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED", "RETURNED"]

  constructor(private apiService: ApiService, private route: ActivatedRoute){}

  ngOnInit(): void {
    this.orderId=this.route.snapshot.paramMap.get('orderId')
    this.fetchOrderDetails()
  }

  fetchOrderDetails():void{
    if (this.orderId) {
      this.apiService.getORderItemById(this.orderId).subscribe({
        next:(res)=>{
          this.orderItem = res.orderItemList || []
          this.orderItem.forEach(item => {
            this.selectedStatus[item.id] =item.status
          })
        },
        error: (err) =>{
          console.log(err)
        }
      })
    }
  }

  handleStatusChange(orderId: number, newStatus: string): void{
    this.selectedStatus[orderId] = newStatus

  }

  handleSubmitStatusChange(orderId: number): void{
    this.apiService.updateOrderItemStatus(orderId.toString(),this.selectedStatus[orderId]).subscribe({
      next: (res) =>{
        this.fetchOrderDetails();
        this.message = "Order Item status was successfully updated"
        setTimeout(()=>{
          this.message =null;
        }, 4000)
      }
    })
  }

}
