import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduleSlotFormComponent } from './schedule-slot-form.component';

describe('ScheduleSlotFormComponent', () => {
  let component: ScheduleSlotFormComponent;
  let fixture: ComponentFixture<ScheduleSlotFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScheduleSlotFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScheduleSlotFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
