import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PendingBookingComponent } from './pending-booking.component';

describe('PendingBookingComponent', () => {
  let component: PendingBookingComponent;
  let fixture: ComponentFixture<PendingBookingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PendingBookingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PendingBookingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
