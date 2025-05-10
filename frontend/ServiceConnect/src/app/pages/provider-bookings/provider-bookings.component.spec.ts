import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProviderBookingsComponent } from './provider-bookings.component';

describe('ProviderBookingsComponent', () => {
  let component: ProviderBookingsComponent;
  let fixture: ComponentFixture<ProviderBookingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProviderBookingsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProviderBookingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
