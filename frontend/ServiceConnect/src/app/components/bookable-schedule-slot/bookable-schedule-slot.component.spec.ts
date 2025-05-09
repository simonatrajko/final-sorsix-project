import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookableScheduleSlotComponent } from './bookable-schedule-slot.component';

describe('BookableScheduleSlotComponent', () => {
  let component: BookableScheduleSlotComponent;
  let fixture: ComponentFixture<BookableScheduleSlotComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookableScheduleSlotComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookableScheduleSlotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
