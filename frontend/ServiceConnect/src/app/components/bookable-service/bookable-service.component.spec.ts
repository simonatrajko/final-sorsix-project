import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookableServiceComponent } from './bookable-service.component';

describe('BookableServiceComponent', () => {
  let component: BookableServiceComponent;
  let fixture: ComponentFixture<BookableServiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookableServiceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookableServiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
