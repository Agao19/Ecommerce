import { Component, EventEmitter, Input } from '@angular/core';

@Component({
  selector: 'app-pagination',
  imports: [],
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent {

  @Input() currentPage: number=1;
  @Input() totalPages: number=1;
  @Input() pageChange = new EventEmitter<number>();

  get pageNumbers(): number[]{
    return Array.from({length: this.totalPages},(_,i) => i+1)
  }

  changepage(page: number): void{
    this.pageChange.emit(page)
  }












}
