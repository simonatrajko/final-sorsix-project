import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProviderFromComponent } from './provider-from.component';

describe('ProviderFromComponent', () => {
  let component: ProviderFromComponent;
  let fixture: ComponentFixture<ProviderFromComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProviderFromComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProviderFromComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
