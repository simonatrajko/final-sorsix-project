import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SeekerFormComponent } from './seeker-form.component';

describe('SeekerFormComponent', () => {
  let component: SeekerFormComponent;
  let fixture: ComponentFixture<SeekerFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SeekerFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SeekerFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
