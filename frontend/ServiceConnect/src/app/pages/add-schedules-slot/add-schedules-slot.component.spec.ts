import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSchedulesSlotComponent } from './add-schedules-slot.component';

describe('AddSchedulesSlotComponent', () => {
  let component: AddSchedulesSlotComponent;
  let fixture: ComponentFixture<AddSchedulesSlotComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddSchedulesSlotComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddSchedulesSlotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
