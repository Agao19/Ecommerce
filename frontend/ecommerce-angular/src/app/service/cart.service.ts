import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cart: any[] = [];

  //localStorage( luu du du lieu o trinh duyet), đầu vào dữ liệu luôn có kiểu string
  //Ham contructor se chay ngay khi khoi tao, vi muon tao gio hang(cart) tu localstorage ngay khi cartservice duoc tao
  //khong su dung ngOnit() vi no chi ton tai trong component, ko ton tai trong service
  constructor() { 
    const savedCart=localStorage.getItem('cart'); //Lấy dữ liệu từ localStorage (key 'cart')
    //neu savedCart tồn tại # null => điều kiện true chạy => Parse JSON từ chuỗi (string) => thành mảng/đối tượng thật
    //nếu savedcart null thì gán this.cart= []
    this.cart=savedCart ? JSON.parse(savedCart) : [];
  }

  getCart(){
    return this.cart;
  }

  getCartItem(itemId: number){
    return this.cart.find(cartItem => cartItem.id ===itemId);
  }




  private saveCart(){
    localStorage.setItem('cart',JSON.stringify(this.cart));
  }

  addItem(item: any){
    const existingItem=this.getCartItem(item.id);
    if(existingItem){
      existingItem.quantity +=1;
    }else{
      this.cart.push({...item,quantity: 1})
    }
    this.saveCart();
  }

  removeItem(itemId: number){
    this.cart = this.cart.filter(item =>item.id !== itemId);
    this.saveCart();
  }

  incrementItem(itemId: number){
    const item = this.getCartItem(itemId); //tim trong this.cart (được lưu ở localstorage) => ko thể dùng quantity2 vì nếu dùng sẽ ko tìm dc
    if(item){
      item.quantity += 1;
      this.saveCart();
    }
  }

  decrementItem(itemId: number){
    const item = this.getCartItem(itemId);
    if(item && item.quantity2 > 1){
      item.quantity -= 1;
      this.saveCart();
    } else{
      this.removeItem(itemId);
    }
  }

  clearCart(){
    this.cart= [];
    localStorage.removeItem('cart')
  }

}
