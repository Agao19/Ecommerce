import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent {

  @Input() currentPage: number=1;
  @Input() totalPages: number=1;
  @Output() pageChange = new EventEmitter<number>();

  get pageNumbers(): number[]{
    return Array.from({length: this.totalPages},(_,i) => i+1)
  }

  changePage(page: number): void{
    this.pageChange.emit(page)
  }












}
