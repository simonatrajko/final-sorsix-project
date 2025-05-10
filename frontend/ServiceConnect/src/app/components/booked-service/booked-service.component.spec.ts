import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookedServiceComponent } from './booked-service.component';

describe('BookedServiceComponent', () => {
  let component: BookedServiceComponent;
  let fixture: ComponentFixture<BookedServiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookedServiceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookedServiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
