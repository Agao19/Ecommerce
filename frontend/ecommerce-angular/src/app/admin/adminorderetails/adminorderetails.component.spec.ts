import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminorderetailsComponent } from './adminorderetails.component';

describe('AdminorderetailsComponent', () => {
  let component: AdminorderetailsComponent;
  let fixture: ComponentFixture<AdminorderetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminorderetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminorderetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
