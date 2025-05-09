import { TestBed } from '@angular/core/testing';

import { ScheduleSlotsService } from './schedule-slots.service';

describe('ScheduleSlotsService', () => {
  let service: ScheduleSlotsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScheduleSlotsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
