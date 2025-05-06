import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduleSlotCardComponent } from './schedule-slot-card.component';

describe('ScheduleSlotCardComponent', () => {
  let component: ScheduleSlotCardComponent;
  let fixture: ComponentFixture<ScheduleSlotCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScheduleSlotCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScheduleSlotCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
