import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduleProviderComponent } from './schedule-provider.component';

describe('ScheduleProviderComponent', () => {
  let component: ScheduleProviderComponent;
  let fixture: ComponentFixture<ScheduleProviderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScheduleProviderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScheduleProviderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
